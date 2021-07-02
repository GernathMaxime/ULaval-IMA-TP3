package ca.ulaval.ima.tp3.customComponant

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ca.ulaval.ima.tp3.R

class EditTextActivity(context: Context, attr: AttributeSet?) :
    ConstraintLayout(context,attr) {
    var title: TextView
    var edit: EditText

    init{
        inflate(context, R.layout.activity_edit_text, this)
        title = findViewById(R.id.topText)
        edit = findViewById(R.id.editText)
    }
}