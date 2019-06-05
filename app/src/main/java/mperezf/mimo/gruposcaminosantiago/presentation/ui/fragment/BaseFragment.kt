package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


open class BaseFragment : Fragment() {

    fun showMessage(msg: String?) {
        view?.let { msg?.let { it1 -> Snackbar.make(it, it1, Snackbar.LENGTH_LONG).show() } }
    }

    fun closeKeyboard() {
        view?.let { v ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }

}
