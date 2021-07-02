package ca.ulaval.ima.tp3


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ca.ulaval.ima.tp3.annonces.MesOffresFragment
import ca.ulaval.ima.tp3.offres.Fragment1
import ca.ulaval.ima.tp3.vendre.Fragment2

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3

)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        lateinit var itemFragment:Fragment
        when(position){
            0 -> itemFragment = Fragment1()
            1 -> itemFragment = Fragment2()
            2 -> itemFragment = MesOffresFragment()
        }
        return itemFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }

}