package ca.ulaval.ima.tp3

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ca.ulaval.ima.tp3.model.SeachOffer
import com.squareup.picasso.Picasso
import java.net.URL

class SeachListAdapter(private val context: Activity, private val offer: MutableList<SeachOffer>) : BaseAdapter() {
    override fun getCount(): Int { return offer.size }

    override fun getItem(position: Int): Any { return offer[position] }

    override fun getItemId(position: Int): Long { return offer[position].id.toLong() }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_car_list, null, true)

        val brandText = rowView.findViewById(R.id.car_brand) as TextView
        val imageView = rowView.findViewById(R.id.carIcon) as ImageView
        val yearText = rowView.findViewById(R.id.car_annee) as TextView
        val kmText = rowView.findViewById(R.id.car_km) as TextView
        val prixText = rowView.findViewById(R.id.car_prix) as TextView

        brandText.text = offer[position].model.brand.name + " " + offer[position].model.name
        Picasso.get().load(offer[position].image).into(imageView);
        yearText.text = offer[position].year.toString()
        kmText.text = offer[position].kilometers.toString()
        prixText.text = offer[position].price.toString()

        return rowView
    }
}