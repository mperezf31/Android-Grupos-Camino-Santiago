package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


open class BaseFragment : Fragment() {

    fun showMessage(msg: String?) {
        view?.let { msg?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_LONG).show() } }
    }

    fun showLoading(show: Boolean) {

    }

}
