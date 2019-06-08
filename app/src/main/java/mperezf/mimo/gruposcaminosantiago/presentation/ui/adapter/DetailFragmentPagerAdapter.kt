package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DetailFragmentPagerAdapter(private val fragmentList: ArrayList<Fragment>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

}