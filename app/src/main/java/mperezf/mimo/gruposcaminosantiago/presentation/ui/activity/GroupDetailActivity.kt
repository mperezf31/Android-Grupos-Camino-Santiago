package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_group_detail.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.DetailFragmentPagerAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.BaseFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupDetailFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupMemberListFragment
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupDetailViewModel


class GroupDetailActivity : AppCompatActivity(), GroupMemberListFragment.MemberFragmentListener,
    ViewPager.OnPageChangeListener {

    companion object {
        const val GROUP_ID = "group_id"
        const val GROUP_TITLE = "group_title"
    }

    private var groupDetail: Group? = null
    private lateinit var viewModel: GroupDetailViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        groupDetail?.let { group ->
            when (item.itemId) {

                R.id.group_detail_tab -> {
                    vp_group_detail.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.group_members_tab -> {
                    vp_group_detail.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.group_chat_tab -> {
                    vp_group_detail.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(GroupDetailViewModel::class.java)
        nav_view_group_detail.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        addObservers()

        intent?.extras?.let { extras ->
            if (extras.containsKey(GROUP_TITLE)) {
                title = extras.getString(GROUP_TITLE)
            }

            if (extras.containsKey(GROUP_ID)) {
                viewModel.getGroupDetail(extras.getInt(GROUP_ID, 0), { group ->
                    showGroupDetailTabs(group)
                }, { error ->
                    Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show()
                })

            } else {
                finish()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }


    private fun showGroupDetailTabs(it: Group) {
        groupDetail = it
        vp_group_detail.adapter = DetailFragmentPagerAdapter(getTabFragments(it), supportFragmentManager)
        vp_group_detail.addOnPageChangeListener(this)

        nav_view_group_detail.visibility = View.VISIBLE
        nav_view_group_detail.selectedItemId = R.id.group_detail_tab
    }

    private fun getTabFragments(group: Group): ArrayList<Fragment> {
        return arrayListOf(
            GroupDetailFragment.newInstance(group),
            GroupMemberListFragment.newInstance(group),
            GroupDetailFragment.newInstance(group)
        )
    }

    private fun addObservers() {

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                pb_group_detail.visibility = View.VISIBLE

            } else {
                pb_group_detail.visibility = View.GONE
            }
        })
    }

    override fun updateGroup(group: Group) {
        groupDetail = group
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> nav_view_group_detail.selectedItemId = R.id.group_detail_tab
            1 -> nav_view_group_detail.selectedItemId = R.id.group_members_tab
            2 -> nav_view_group_detail.selectedItemId = R.id.group_chat_tab
        }
    }


}
