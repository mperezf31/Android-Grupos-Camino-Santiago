package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog.LogoutDialogFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupListFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.SettingsFragment
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.MainViewModel


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    LogoutDialogFragment.OnLogoutListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvDrawerTitle: TextView
    private lateinit var tvDrawerSubTitle: TextView
    private lateinit var ivDrawerAvatar: ImageView
    private lateinit var viewModel: MainViewModel

    private var authenticatedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setCheckedItem(R.id.nav_groups)
        navView.setNavigationItemSelectedListener(this)

        tvDrawerTitle = navView.getHeaderView(0).findViewById(R.id.nav_header_title)
        tvDrawerSubTitle = navView.getHeaderView(0).findViewById(R.id.nav_header_subtitle)
        ivDrawerAvatar = navView.getHeaderView(0).findViewById(R.id.iv_drawer_avatar)

        viewModel.getAuthenticatedUser({
            authenticatedUser = it
            showUserInfo(it)
            showFragment(GroupListFragment.newInstance())
        }, {
            goToLogin()
        })
    }

    private fun showUserInfo(user: User) {
        user.photo?.let { it -> ivDrawerAvatar.fromBase64(it) }
        tvDrawerTitle.text = user.name
        tvDrawerSubTitle.text = user.email
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_groups -> {
                showFragment(GroupListFragment.newInstance())
            }

            R.id.nav_settings -> {
                showFragment(SettingsFragment.newInstance())
            }

            R.id.nav_share -> {
            }

            R.id.nav_logout -> {
                LogoutDialogFragment.newInstance().show(supportFragmentManager, LogoutDialogFragment.TAG)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }


    override fun logoutConfirmed() {
        viewModel.logout {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
