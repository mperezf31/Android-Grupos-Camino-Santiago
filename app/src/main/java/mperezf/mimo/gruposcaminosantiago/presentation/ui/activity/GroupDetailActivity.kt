package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_group_detail.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.presentation.ui.adapter.DetailFragmentPagerAdapter
import mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog.DeleteGroupDialogFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupListFragment
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.GroupDetailViewModel


class GroupDetailActivity : BaseActivity(),UpdateGroupDetailListener,
    ViewPager.OnPageChangeListener , DeleteGroupDialogFragment.OnDeleteGroupListener {

    companion object {
        const val GROUP_ID = "group_id"
        const val GROUP_TITLE = "group_title"
    }

    private var groupDetail: Group? = null
    private lateinit var viewModel: GroupDetailViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        groupDetail?.let { _ ->
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
        vp_group_detail.setPageTransformer(true, ZoomOutPageTransformer())

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_delete_group -> {
                confirmDeleteGroup()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDeleteGroup() {
        DeleteGroupDialogFragment.newInstance().show(supportFragmentManager,DeleteGroupDialogFragment.TAG)
    }

    override fun deleteGroupConfirmed() {
        groupDetail?.id?.let {
            viewModel.deleteGroup(it,{

                groupDeleted()
            },{
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
            })
        }
    }


    private fun groupDeleted() {
        val output = Intent()
        output.putExtra(GroupListFragment.RELOAD_LIST, true)
        setResult(Activity.RESULT_OK, output)
        finish()
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
        vp_group_detail.adapter = DetailFragmentPagerAdapter(it, supportFragmentManager)
        vp_group_detail.addOnPageChangeListener(this)

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

        closeKeyboard()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}


private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

class ZoomOutPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}


interface UpdateGroupDetailListener {

    fun updateGroup(group: Group)
}