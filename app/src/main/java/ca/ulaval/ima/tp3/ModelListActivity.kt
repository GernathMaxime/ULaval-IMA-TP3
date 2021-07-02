package ca.ulaval.ima.tp3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import ca.ulaval.ima.tp3.model.Brand
import ca.ulaval.ima.tp3.model.Model
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModelListActivity : AppCompatActivity() {

    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)
    var IdBrand: Int = 1
    var Brand: String = ""
    var IdModel: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Modèles de voiture")
        setContentView(R.layout.activity_model_list)
        IdBrand += intent.getStringExtra("id")!!.toInt()
        Brand += intent.getStringExtra("BrandName")
        getListOfModel()
    }

    private fun getListOfModel(){
        tp3NetworkCenter.listbrandmodel(IdBrand).enqueue(object :
            Callback<TP3API.ContentResponse<List<Model>>> {
            override fun onResponse(
                call: Call<TP3API.ContentResponse<List<Model>>>,
                response: Response<TP3API.ContentResponse<List<Model>>>
            ) {
                var centerlist : List<String> = emptyList()
                response.body()?.content?.let {
                    for (model in it) {
                        centerlist = centerlist + model.name
                        IdModel.add(model.id)
                        Log.d(
                            "ima-demo",
                            "Got Restaurant ${model.name} with id ${model.id}"
                        )
                    }
                    val lv = findViewById<ListView>(R.id.listview)
                    val adapter = ArrayAdapter(this@ModelListActivity, android.R.layout.simple_list_item_1, centerlist)
                    lv.adapter = adapter
                    lv.setOnItemClickListener { parent, view, position, id ->
                        val element = adapter.getItem(position)
                        Log.d("ima-demo", element.toString())// The item that was clicked
                        val intent = Intent(this@ModelListActivity, SearchListActivity::class.java)
                        intent.putExtra("Brand", IdBrand.toString())
                        intent.putExtra("Model", IdModel[position].toString())
                        intent.putExtra("Car",  Brand + " " + element)
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