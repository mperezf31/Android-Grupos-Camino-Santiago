package mperezf.mimo.gruposcaminosantiago.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import mperezf.mimo.gruposcaminosantiago.R

abstract class BaseActivity : AppCompatActivity() {


    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.container, fragment)
            .commitNow()
    }


    fun showMessage(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }
}
