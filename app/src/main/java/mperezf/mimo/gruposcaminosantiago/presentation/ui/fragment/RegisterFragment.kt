package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.register_fragment.*
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.extension.resize
import mperezf.mimo.gruposcaminosantiago.presentation.extension.toBase64
import mperezf.mimo.gruposcaminosantiago.presentation.extension.validate
import mperezf.mimo.gruposcaminosantiago.presentation.viewModel.RegisterViewModel


class RegisterFragment : BaseFragment() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2

        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    private var registerFragmentListener: RegisterFragmentListener? = null
    private var bitmapAvatar: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(mperezf.mimo.gruposcaminosantiago.R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = RegisterViewModel.Factory(application = activity?.application as CaminoDeSantiagoApp)
        viewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel::class.java)

        addObservers()
        addListeners()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        registerFragmentListener = activity as RegisterFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        registerFragmentListener = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            goToGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImage: Uri? = data?.data
                    bitmapAvatar = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
                    iv_avatar.setImageBitmap(bitmapAvatar)
                }
            }
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
                bt_create_account.startAnimation()

            } else {
                bt_create_account.revertAnimation()
            }
        })

        viewModel.getFinishRegister().observe(this, Observer<Boolean> {
            registerFragmentListener?.initApp()
        })

    }

    private fun addListeners() {

        tv_go_to_login.setOnClickListener { registerFragmentListener?.showLogin() }

        bt_select_avatar.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
                if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    goToGallery()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                }
            } else {
                goToGallery()
            }

        }

        bt_create_account.setOnClickListener {
            closeKeyboard()

            if (validateForm()) {
                if (et_password.text.toString() != et_confirm_password.text.toString()) {
                    showMessage(getString(R.string.passwords_not_match))
                } else {
                    val newUser = User(
                        photo = bitmapAvatar?.resize(300, 300)?.toBase64(),
                        name = et_name.text.toString(),
                        email = et_email.text.toString(),
                        password = et_password.text.toString()
                    )
                    viewModel.register(newUser)
                }
            }
        }

    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun validateForm(): Boolean {
        val validations = arrayOf(
            et_name.validate(true)
            , et_email.validate(true, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            , et_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD)
            , et_confirm_password.validate(true, InputType.TYPE_TEXT_VARIATION_PASSWORD)
        )
        return !validations.contains(false)
    }


    interface RegisterFragmentListener {
        fun showLogin()
        fun initApp()
    }

}
