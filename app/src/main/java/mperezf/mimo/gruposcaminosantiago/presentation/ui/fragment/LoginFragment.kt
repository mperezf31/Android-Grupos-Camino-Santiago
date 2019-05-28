package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.login_fragment.*
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel.LoginViewModel

class LoginFragment : Fragment() {

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginFragmentListener = activity as LoginFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentListener = null
    }

    interface LoginFragmentListener {
        fun showRegister()
    }
}
