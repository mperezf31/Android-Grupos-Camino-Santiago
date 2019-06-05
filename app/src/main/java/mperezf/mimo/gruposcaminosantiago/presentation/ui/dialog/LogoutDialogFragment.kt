package mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import mperezf.mimo.gruposcaminosantiago.R

class LogoutDialogFragment : DialogFragment(){

    companion object {
        val TAG: String = LogoutDialogFragment::class.java.simpleName

        fun newInstance() = LogoutDialogFragment()
    }

    private var onLogoutListener: OnLogoutListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onLogoutListener = context as OnLogoutListener
    }

    override fun onDetach() {
        onLogoutListener = null
        super.onDetach()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity,R.style.AlertDialogTheme)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onLogoutListener?.logoutConfirmed()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }

        return builder.create()
    }

    interface OnLogoutListener {
        fun logoutConfirmed()
    }

}