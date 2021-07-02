package ca.ulaval.ima.tp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.ulaval.ima.tp3.model.DetailOffer
import ca.ulaval.ima.tp3.network.NetworkCenter
import ca.ulaval.ima.tp3.network.TP3API
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilCarActivity : AppCompatActivity() {
    private val tp3NetworkCenter = NetworkCenter.buildService(TP3API::class.java)
    var IdOffer: Int = 0
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Vendre une voiture")
        setContentView(R.layout.activity_profil_car)
        IdOffer += intent.getStringExtra("OfferId")!!.toInt()

        getOffer()
    }

    private fun getOffer(){
        val image = findViewById<ImageView>(R.id.image)
        val marque = findViewById<TextView>(R.id.marque)
        val model = findViewById<TextView>(R.id.model)
        val année = findViewById<TextView>(R.id.année)
        val km = findViewById<TextView>(R.id.km)
        val transmission = findViewById<TextView>(R.id.transmission)
        val prix = findViewById<TextView>(R.id.prix)
        val nom = findViewById<TextView>(R.id.nom)
        val courriel = findViewById<TextView>(R.id.courriel)
        val propriétaire = findViewById<TextView>(R.id.propriétaire)
        val description = findViewById<TextView>(R.id.description)
        val emailButton = findViewById<Button>(R.id.button2)

        emailButton.setOnClickListener {

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:abc@xyz.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))

        }

        tp3NetworkCenter.detailoffer(IdOffer).enqueue(object :
            Callback<TP3API.ContentResponse<DetailOffer>> {
            override fun onResponse(
                call: Call<TP3API.ContentResponse<DetailOffer>>,
                response: Response<TP3API.ContentResponse<DetailOffer>>
            ) {
                response.body()?.content?.let {
                    Picasso.get().load(it.image).into(image);
                    marque.text = it.model.brand.name
                    model.text = it.model.name
                    année.text = it.year.toString()
                    km.text = it.kilometers.toString()
                    transmission.text = it.transmission.toString()
                    prix.text = it.price.toString()
                    nom.text = it.seller.first_name
                    courriel.text = it.seller.email
                    email = it.seller.email
                    if (it.from_owner == false) {
                        propriétaire.text = "Non"
                    } else {
                        propriétaire.text = "Oui"
                    }
                    description.text = it.description
                }
            }

            override fun onFailure(
                call: Call<TP3API.ContentResponse<DetailOffer>>,
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