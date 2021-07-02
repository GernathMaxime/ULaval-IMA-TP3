package ca.ulaval.ima.tp3.offres

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import ca.ulaval.ima.tp3.MainActivity
import ca.ulaval.ima.tp3.ModelListActivity
import ca.ulaval.ima.tp3.R
import ca.ulaval.ima.tp3.model.Brand
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Fragment1 : Fragment() {

    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        getListOfBrand()
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    private fun getListOfBrand(){
        tp3NetworkCenter.listbrand().enqueue(object :
            Callback<TP3API.ContentResponse<List<Brand>>> {
            override fun onResponse(
                call: Call<TP3API.ContentResponse<List<Brand>>>,
                response: Response<TP3API.ContentResponse<List<Brand>>>
            ) {
                var centerlist : List<String> = emptyList()
                response.body()?.content?.let {
                    for (brand in it) {
                        centerlist = centerlist + brand.name
                    }
                    val context = context as MainActivity

                    val lv = context.findViewById(R.id.listview) as ListView
                    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, centerlist)
                    lv.adapter = adapter
                    lv.setOnItemClickListener { parent, view, position, id ->
                        val element = adapter.getItem(position)
                        Log.d("ima-demo", element.toString())// The item that was clicked
                        Log.d("ima-demo", position.toString())
                        val intent = Intent(requireContext(), ModelListActivity::class.java)
                        intent.putExtra("id", position.toString())
                        intent.putExtra("BrandName", element)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(
                call: Call<TP3API.ContentResponse<List<Brand>>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure $t")
            }

        }

        )
    }
}