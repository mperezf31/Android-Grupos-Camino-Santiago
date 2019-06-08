package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.LoginFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.RegisterFragment

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentListener,
    RegisterFragment.RegisterFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        if (savedInstanceState == null) {
            showLogin()
        }
    }

    override fun showRegister() {
        showFragment(RegisterFragment.newInstance())
    }

    override fun showLogin() {
        showFragment(LoginFragment.newInstance())
    }

    override fun initApp() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.container, fragment)
            .commitNow()
    }
}
