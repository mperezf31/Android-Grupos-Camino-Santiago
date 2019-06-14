package mperezf.mimo.gruposcaminosantiago.presentation.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import mperezf.mimo.gruposcaminosantiago.R

class DeleteGroupDialogFragment : DialogFragment(){

    companion object {
        val TAG: String = DeleteGroupDialogFragment::class.java.simpleName

        fun newInstance() = DeleteGroupDialogFragment()
    }

    private var deleteGroupListener: OnDeleteGroupListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        deleteGroupListener = context as OnDeleteGroupListener
    }

    override fun onDetach() {
        deleteGroupListener = null
        super.onDetach()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity,R.style.AlertDialogTheme)
            .setTitle(getString(R.string.delete_group))
            .setMessage(getString(R.string.delete_group_comfirmation))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteGroupListener?.deleteGroupConfirmed()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }

        return builder.create()
    }

    interface OnDeleteGroupListener {
        fun deleteGroupConfirmed()
    }

}