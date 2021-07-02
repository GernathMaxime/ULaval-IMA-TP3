package ca.ulaval.ima.tp3.customComponant

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ca.ulaval.ima.tp3.R

class SpinnerActivity(context: Context, attr: AttributeSet?) :
    ConstraintLayout(context,attr) {
    var title: TextView
    var spinner: Spinner

    init{
        inflate(context, R.layout.activity_spinner, this)
        title = findViewById(R.id.topText)
        spinner = findViewById(R.id.seekBar)
    }
}