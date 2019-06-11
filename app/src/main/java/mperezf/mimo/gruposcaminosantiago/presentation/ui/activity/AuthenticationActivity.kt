package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.LoginFragment
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.RegisterFragment

class AuthenticationActivity : BaseActivity(), LoginFragment.LoginFragmentListener,
    RegisterFragment.RegisterFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_activity)

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

}
