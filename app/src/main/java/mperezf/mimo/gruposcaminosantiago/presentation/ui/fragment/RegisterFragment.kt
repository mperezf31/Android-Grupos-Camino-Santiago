package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.register_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.extension.validate
import mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel.RegisterViewModel

class RegisterFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private var loginFragmentListener: RegisterFragmentListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_go_to_login.setOnClickListener { loginFragmentListener?.showLogin() }
        bt_create_account.setOnClickListener {
            if (validateForm()) {
                Log.e("Valid data", "next step")
            }
        }
    }


    private fun validateForm(): Boolean {
        return (et_name.validate(true)
                && et_email.validate(true, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                && et_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD)
                && et__confirm_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentListener = activity as RegisterFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentListener = null
    }

    interface RegisterFragmentListener {
        fun showLogin()
    }

}
