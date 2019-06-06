package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_group_detail.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupDetailFragment
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupDetailViewModel


class GroupDetailActivity : AppCompatActivity() {

    companion object {
        const val GROUP_ID = "group_id"
        const val GROUP_TITLE = "group_title"
    }

    private var groupDetail: Group? = null
    private lateinit var viewModel: GroupDetailViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        groupDetail?.let {
            when (item.itemId) {

                R.id.group_detail_tab -> {
                    showFragment(GroupDetailFragment.newInstance(it))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.group_members_tab -> {
                    showFragment(GroupDetailFragment.newInstance(it))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.group_chat_tab -> {
                    showFragment(GroupDetailFragment.newInstance(it))
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
            if (extras.containsKey(GROUP_TITLE)){
                title = extras.getString(GROUP_TITLE)
            }

            if (extras.containsKey(GROUP_ID)) {
                viewModel.getGroupDetail(extras.getInt(GROUP_ID, 0), { group ->
                    showGroupDetailTabs(group)
                }, {
                    Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
                })

            } else {
                finish()
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.detail_container, fragment)
            .commitNow()
    }

    private fun showGroupDetailTabs(it: Group) {
        groupDetail = it

        nav_view_group_detail.visibility = View.VISIBLE
        nav_view_group_detail.selectedItemId = R.id.group_detail_tab
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


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
