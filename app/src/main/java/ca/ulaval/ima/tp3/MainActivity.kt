package ca.ulaval.ima.tp3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ca.ulaval.ima.tp3.annonces.MesOffresFragment
import ca.ulaval.ima.tp3.model.SaveState
import ca.ulaval.ima.tp3.vendre.Fragment2
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), Fragment2.MyListener, MesOffresFragment.TokenListener{
    var saveState = SaveState(0, null, null, null, null, null, null, null,null)
    var myToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        setUpState()
        viewPager.setCurrentItem(getIntent().getIntExtra("EXTRA", 0))
        tabs.setupWithViewPager(viewPager)
    }

    fun setUpState() {
        saveState.km = "10000"
        saveState.year = 2
        saveState.transmission = 0
        saveState.price = "35000"
        saveState.owner = true

        saveState.brand = getIntent().getIntExtra("idBrand", 1)
        saveState.model = getIntent().getIntExtra("idModel", 1)
        saveState.carName = getIntent().getStringExtra("car")

        if (saveState.carName == null) {
            saveState.carName = "Acura MDX"
        }

    }

    override fun getState(): SaveState {
        return saveState
    }

    override fun setState(state: SaveState) {
        saveState = state
    }

    override fun getToken(): String {
        return myToken
    }

    override fun setToken(token: String) {
        myToken = token
    }
}