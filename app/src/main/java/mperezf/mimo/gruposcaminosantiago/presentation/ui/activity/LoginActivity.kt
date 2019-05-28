package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.os.Bundle
import android.transition.Fade
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.LoginFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.RegisterFragment

class LoginActivity : AppCompatActivity() , LoginFragment.LoginFragmentListener ,
    RegisterFragment.RegisterFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
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

    private fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.container, fragment)
            .commitNow()
    }
}
