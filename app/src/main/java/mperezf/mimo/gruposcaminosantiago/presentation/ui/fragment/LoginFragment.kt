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
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.extension.validate
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.LoginViewModel

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

        val factory = LoginViewModel.Factory(application = activity?.application as CaminoDeSantiagoApp)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

        addObservers()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentListener = activity as LoginFragmentListener
    }

    override fun onDetach() {
        loginFragmentListener = null
        super.onDetach()
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }

    private fun addObservers() {
        viewModel.getErrorMsg().observe(this, Observer<String> {
            showMessage(it)
        })

        viewModel.getLoadingState().observe(this, Observer<Boolean> {
            if (it) {
                bt_login.startAnimation()

            } else {
                bt_login.revertAnimation()
            }
        })

        viewModel.getFinishLogin().observe(this, Observer<Boolean> {
            loginFragmentListener?.initApp()
        })

    }


    private fun validateForm(): Boolean {
        val validations = arrayOf(
            et_email.validate(true, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            , et_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD)
        )
        return !validations.contains(false)
    }


    interface LoginFragmentListener {
        fun showRegister()

        fun initApp()
    }
}
