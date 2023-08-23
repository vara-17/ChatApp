package vara17.chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int) = fragmentList[position]

    fun addFragment(fragment: Fragment) = fragmentList.add(fragment)

}