package ca.ulaval.ima.tp3.vendre

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import ca.ulaval.ima.tp3.AllModelListActivity
import ca.ulaval.ima.tp3.ProfilCarActivity
import ca.ulaval.ima.tp3.R
import ca.ulaval.ima.tp3.customComponant.EditTextActivity
import ca.ulaval.ima.tp3.customComponant.SpinnerActivity
import ca.ulaval.ima.tp3.model.*
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class Fragment2 : Fragment() {
    var trans = arrayOf("MA", "AT", "SM")
    var mCallback: MyListener? = null
    var saveState: SaveState = SaveState(0, null, null, null, null, null, null, null,null)
    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = activity as MyListener
    }

    interface MyListener {
        fun getState(): SaveState
        fun getToken(): String
        fun setToken(token: String)
        fun setState(state: SaveState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_2, container, false)
        saveState = mCallback!!.getState()
        val modelbutton = root.findViewById<Button>(R.id.modelbutton)
        val yearspinner = root.findViewById<SpinnerActivity>(R.id.yearspinner)
        val kmtext = root.findViewById<EditTextActivity>(R.id.kmtext)
        val transmissionspinner = root.findViewById<SpinnerActivity>(R.id.transmissionspinner)
        val prixtext = root.findViewById<EditTextActivity>(R.id.prixtext)
        val checkBox = root.findViewById<CheckBox>(R.id.checkBox)
        val soumettrebutton = root.findViewById<Button>(R.id.soumettrebutton)

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.transmission_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            transmissionspinner.spinner.adapter = adapter
        }

        val years = ArrayList<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1950..thisYear) {
            years.add(Integer.toString(i))
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                years
        )
        yearspinner.spinner.adapter = adapter


        val State = mCallback?.getState()
        yearspinner.title.text = "Année:"
        kmtext.title.text = "Kilomètrage:"
        transmissionspinner.title.text = "Type de transmission"
        prixtext.title.text = "Prix:"

        State!!.year?.let { yearspinner.spinner.setSelection(it) }
        kmtext.edit.setText(State.km.toString())
        State.transmission?.let { transmissionspinner.spinner.setSelection(it) }
        prixtext.edit.setText(State.price.toString())
        modelbutton.text = State.carName
        State.owner?.let { checkBox.setChecked(it) }

        modelbutton.setOnClickListener{
            saveState.owner = checkBox.isChecked
            saveState.price = prixtext.edit.text.toString()
            saveState.km = kmtext.edit.text.toString()
            saveState.transmission = transmissionspinner.spinner.selectedItemPosition
            saveState.year = yearspinner.spinner.selectedItemPosition
            mCallback!!.setState(saveState)
            val intent = Intent(requireContext(), AllModelListActivity::class.java)
            startActivity(intent)
        }

        soumettrebutton.setOnClickListener{
            saveState.owner = checkBox.isChecked
            saveState.price = prixtext.edit.text.toString()
            saveState.km = kmtext.edit.text.toString()
            saveState.transmission = transmissionspinner.spinner.selectedItemPosition
            saveState.year = yearspinner.spinner.selectedItemPosition
            mCallback!!.setState(saveState)
            getexpiredtoken(saveState)
        }
        return root
    }

    private fun getexpiredtoken(state: SaveState) {
        tp3NetworkCenter.getaccount("Basic " + mCallback!!.getToken()).enqueue(object :
                Callback<TP3API.ContentResponse<Account>> {
            override fun onResponse(
                    call: Call<TP3API.ContentResponse<Account>>,
                    response: Response<TP3API.ContentResponse<Account>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        Log.d("ima-demo", it.email)
                        addOffer(state)
                    }
                } else {
                    Log.d("ima-demo",  response.toString())
                    withEditText(requireView(),state)
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

    private fun addOffer(state : SaveState) {
        val params: MutableMap<String, String> = HashMap()
        params["from_owner"] = state.owner.toString()
        params["kilometers"] = state.km.toString()
        params["year"] = (state.year?.plus(1950)).toString()
        params["price"] = state.price.toString()
        params["transmission"] = trans[state.transmission!!]
        params["model"] = state.model.toString()

        tp3NetworkCenter.addOffer(params, "Basic " + mCallback!!.getToken()).enqueue(object :
                Callback<TP3API.ContentResponse<DetailOffer>> {
            override fun onResponse(
                    call: Call<TP3API.ContentResponse<DetailOffer>>,
                    response: Response<TP3API.ContentResponse<DetailOffer>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        val intent = Intent(requireContext(), ProfilCarActivity::class.java)
                        intent.putExtra("OfferId", it.id.toString())
                        startActivity(intent)
                    }
                } else {
                    Log.d("ima-demo",  response.toString())
                    withEditText(requireView(), state)
                }
            }

            override fun onFailure(
                    call: Call<TP3API.ContentResponse<DetailOffer>>,
                    t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure $t")
            }

        }
        )
    }

    private fun login(login: Login, state: SaveState){
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
                        getexpiredtoken(state)
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

    fun withEditText(view: View, state: SaveState) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.connection_alert, null)
        val emailEditText  = dialogLayout.findViewById<EditTextActivity>(R.id.email)
        val niEditText  = dialogLayout.findViewById<EditTextActivity>(R.id.ni)

        builder.setNegativeButton("OK", { dialog, whichButton ->
            Log.d("ima-demo", "yes")
            login(Login(emailEditText.edit.text.toString(), niEditText.edit.text.toString()), state)
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