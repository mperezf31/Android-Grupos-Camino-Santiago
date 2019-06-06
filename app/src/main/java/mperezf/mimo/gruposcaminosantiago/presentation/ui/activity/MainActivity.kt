package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.extension.fromBase64
import mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog.LogoutDialogFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.GroupListFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.SettingsFragment
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.MainViewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    LogoutDialogFragment.OnLogoutListener {

    private lateinit var viewModel: MainViewModel

    private var authenticatedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setCheckedItem(R.id.nav_groups)
        nav_view.setNavigationItemSelectedListener(this)

        viewModel.getAuthenticatedUser({
            authenticatedUser = it
            showUserInfo(it)
            showFragment(GroupListFragment.newInstance(it.id!!))
        }, {
            goToLogin()
        })
    }


    private fun showUserInfo(user: User) {
        user.photo?.let { it -> iv_drawer_avatar.fromBase64(it) }
        nav_header_title.text = user.name
        nav_header_subtitle.text = user.email
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.main_container, fragment)
            .commitNow()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_groups -> {
                showFragment(GroupListFragment.newInstance(authenticatedUser?.id!!))
            }
            R.id.nav_settings -> {
                showFragment(SettingsFragment())
            }


            R.id.nav_share -> {
            }
            R.id.nav_logout -> {
                LogoutDialogFragment.newInstance().show(supportFragmentManager, LogoutDialogFragment.TAG)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun logoutConfirmed() {
        viewModel.logout {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
