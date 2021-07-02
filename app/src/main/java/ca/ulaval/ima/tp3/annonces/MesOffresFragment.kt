package ca.ulaval.ima.tp3.annonces

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ca.ulaval.ima.tp3.ProfilCarActivity
import ca.ulaval.ima.tp3.R
import ca.ulaval.ima.tp3.SeachListAdapter
import ca.ulaval.ima.tp3.customComponant.EditTextActivity
import ca.ulaval.ima.tp3.model.*
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.set


class MesOffresFragment : Fragment() {
    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)
    var mCallback: TokenListener? = null
    var Result: MutableList<SeachOffer> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = activity as TokenListener
    }

    interface TokenListener {
        fun getToken(): String
        fun setToken(token: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ima-demo", "maybe")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ima-demo", "alors que")
        getexpiredtoken()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_mesoffres, container, false)
        Log.d("ima-demo", "response.toString()")
        return root
    }

    override fun setMenuVisibility(isvisible: Boolean) {
        super.setMenuVisibility(isvisible)
        if (isvisible) {

            Log.d("ima-demo", "fragment is visible ")
        } else {
            Log.d("ima-demo", "fragment is not visible ")
        }
    }

    private fun login(login: Login){
        val params: MutableMap<String, String> = HashMap()
        params["email"] = login.email
        params["identification_number"] = login.ni

        tp3NetworkCenter.login(params).enqueue(object :
                Callback<TP3API.ContentResponse<Token>> {
            override fun onResponse(
                    call: Call<TP3API.ContentResponse<Token>>,
                    response: Response<TP3API.ContentResponse<Token>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        mCallback!!.setToken(it.token)
                        getListOfResult()
                    }
                } else {
                    Log.d("ima-demo", response.toString())
                }
            }

            override fun onFailure(
                    call: Call<TP3API.ContentResponse<Token>>,
                    t: Throwable
            ) {
                Log.d("ima-demo", t.toString())
            }

        }
        )
    }

    private fun getListOfResult() {
        tp3NetworkCenter.myOffer("Basic " + mCallback!!.getToken()).enqueue(object :
                Callback<TP3API.ContentResponse<List<SeachOffer>>> {
            override fun onResponse(
                    call: Call<TP3API.ContentResponse<List<SeachOffer>>>,
                    response: Response<TP3API.ContentResponse<List<SeachOffer>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        for (result in it) {
                            Result.add(result)
                        }
                        val lv = requireView().findViewById<ListView>(R.id.carlistview)
                        val adapter = SeachListAdapter(requireActivity(), Result)
                        lv.adapter = adapter
                        lv.setOnItemClickListener { adapterView, view, position, id ->
                            val element = adapterView.getItemAtPosition(position)
                            Log.d("ima-demo", element.toString())// The item that was clicked
                            val intent = Intent(requireContext(), ProfilCarActivity::class.java)
                            intent.putExtra("OfferId", Result[position].id.toString())
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.d("ima-demo", response.toString())
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
    private fun getexpiredtoken() {
        tp3NetworkCenter.getaccount("Basic " + mCallback!!.getToken()).enqueue(object :
                Callback<TP3API.ContentResponse<Account>> {
            override fun onResponse(
                    call: Call<TP3API.ContentResponse<Account>>,
                    response: Response<TP3API.ContentResponse<Account>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        Log.d("ima-demo", it.email)
                        getListOfResult()
                    }
                } else {
                    Log.d("ima-demo",  response.toString())
                    withEditText(requireView())
                }
            }

            override fun onFailure(
                    call: Call<TP3API.ContentResponse<Account>>,
                    t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure $t")
            }

        }
        )
    }
    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.connection_alert, null)
        val emailEditText  = dialogLayout.findViewById<EditTextActivity>(R.id.email)
        val niEditText  = dialogLayout.findViewById<EditTextActivity>(R.id.ni)

        builder.setNegativeButton("OK", { dialog, whichButton ->
            login(Login(emailEditText.edit.text.toString(), niEditText.edit.text.toString()))
            dialog.dismiss()
        })
        emailEditText.title.setText("Email:")
        emailEditText.edit.setText("maxime.gernath.1@ulaval.ca")
        niEditText.title.setText("Numéro d’identification:")
        niEditText.edit.setText("536793090")
        builder.setView(dialogLayout)
        builder.show()
    }
}