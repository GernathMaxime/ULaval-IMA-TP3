package ca.ulaval.ima.tp3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import ca.ulaval.ima.tp3.model.Model
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import ca.ulaval.ima.tp3.vendre.Fragment2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ModelListFragment : Fragment() {

    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_model_list, container, false)
        // Inflate the layout for this fragment
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
                    }
                    val lv = root.findViewById<ListView>(R.id.listview)
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, centerlist)
                    lv.adapter = adapter
                    lv.setOnItemClickListener { parent, view, position, id ->
                        val element = adapter.getItem(position)
                        Log.d("ima-demo", element.toString())// The item that was clicked
                        val intent = Intent(requireContext(), Fragment2::class.java)
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
        return inflater.inflate(R.layout.fragment_model_list, container, false)
    }

}