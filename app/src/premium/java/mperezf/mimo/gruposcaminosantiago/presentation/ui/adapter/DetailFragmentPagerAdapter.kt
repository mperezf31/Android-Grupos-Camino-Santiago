package mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupChatFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupDetailFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupMemberListFragment

class DetailFragmentPagerAdapter(group: Group, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val fragmentList: ArrayList<Fragment> = getTabFragments(group)

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    private fun getTabFragments(group: Group): ArrayList<Fragment> {
        return arrayListOf(
            GroupDetailFragment.newInstance(group),
            GroupMemberListFragment.newInstance(group),
            GroupChatFragment.newInstance(group)
        )
    }

}