package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.login_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.extension.validate
import mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel.LoginViewModel

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var loginFragmentListener: LoginFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_create_account.setOnClickListener {
            loginFragmentListener?.showRegister()
        }

        bt_login.setOnClickListener {
            closeKeyboard()
            if (validateForm()) {
                viewModel.doLogin(et_email.text.toString(), et_password.text.toString())
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        addObservers()
    }

    private fun addObservers() {
        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                bt_login.isEnabled = false
                bt_login.startAnimation()

            } else {
                bt_login.revertAnimation()
                bt_login.isEnabled = true
            }
        })

        viewModel.getFinishLogin().observe(this, Observer<Boolean> {
            loginFragmentListener?.finishLogin()
        })

    }


    private fun validateForm(): Boolean {
        return (et_email.validate(true, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                && et_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentListener = activity as LoginFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentListener = null
    }

    override fun onDestroy() {
        bt_login.dispose()
        viewModel.dispose()
        super.onDestroy()

    }

    interface LoginFragmentListener {
        fun showRegister()

        fun finishLogin()
    }
}
