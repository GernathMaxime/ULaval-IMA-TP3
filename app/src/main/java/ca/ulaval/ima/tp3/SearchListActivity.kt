package ca.ulaval.ima.tp3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import ca.ulaval.ima.tp3.model.Model
import ca.ulaval.ima.tp3.model.SeachOffer
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchListActivity : AppCompatActivity() {
    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)
    var IdBrand: Int = 0
    var IdModel: Int = 0
    var Result: MutableList<SeachOffer> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_search_list)
        IdBrand += intent.getStringExtra("Brand")!!.toInt()
        IdModel += intent.getStringExtra("Model")!!.toInt()
        setTitle(intent.getStringExtra("Car"))
        getListOfResult()
    }

    private fun getListOfResult(){
        tp3NetworkCenter.listsearch(IdBrand, IdModel).enqueue(object :
            Callback<TP3API.ContentResponse<List<SeachOffer>>> {
            override fun onResponse(
                call: Call<TP3API.ContentResponse<List<SeachOffer>>>,
                response: Response<TP3API.ContentResponse<List<SeachOffer>>>
            ) {
                response.body()?.content?.let {
                    for (result in it) {
                        Result.add(result)
                        Log.d(
                            "ima-demo",
                            "Got Restaurant ${result.model.name} with id ${result.year}"
                        )
                    }
                    val lv = findViewById<ListView>(R.id.listview)
                    val adapter = SeachListAdapter(this@SearchListActivity,Result)
                    lv.adapter = adapter
                    lv.setOnItemClickListener { adapterView, view, position, id ->
                        val element = adapterView.getItemAtPosition(position)
                        Log.d("ima-demo", element.toString())// The item that was clicked
                        val intent = Intent(this@SearchListActivity, ProfilCarActivity::class.java)
                        intent.putExtra("OfferId", Result[position].id.toString())
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(
                call: Call<TP3API.ContentResponse<List<SeachOffer>>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure $t")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}