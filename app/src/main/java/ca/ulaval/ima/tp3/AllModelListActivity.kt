package ca.ulaval.ima.tp3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import ca.ulaval.ima.tp3.model.Model
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import ca.ulaval.ima.tp3.vendre.Fragment2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllModelListActivity : AppCompatActivity() {
    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)
    var IdModel: MutableList<Int> = ArrayList()
    var IdBrand: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Modèles de voiture")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_model_list)
        getListOfModel()
    }

    private fun getListOfModel(){
        tp3NetworkCenter.listmodel().enqueue(object :
            Callback<TP3API.ContentResponse<List<Model>>> {
            override fun onResponse(
                call: Call<TP3API.ContentResponse<List<Model>>>,
                response: Response<TP3API.ContentResponse<List<Model>>>
            ) {
                var centerlist = mutableListOf<String>()
                response.body()?.content?.let {
                    for (model in it) {
                        centerlist.add(model.brand.name + " " + model.name)
                        IdModel.add(model.id)
                        IdBrand.add(model.brand.id)
                    }
                    val lv = findViewById<ListView>(R.id.listview)
                    val adapter = ArrayAdapter(this@AllModelListActivity, android.R.layout.simple_list_item_1, centerlist)
                    lv.adapter = adapter
                    lv.setOnItemClickListener { parent, view, position, id ->
                        val element = adapter.getItem(position)
                        val intent = Intent(this@AllModelListActivity, MainActivity::class.java)
                        intent.putExtra("EXTRA", 1)
                        intent.putExtra("car", element)
                        intent.putExtra("idBrand", IdBrand[position])
                        intent.putExtra("idModel", IdModel[position])
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(
                call: Call<TP3API.ContentResponse<List<Model>>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure $t")
            }

        }

        )
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}