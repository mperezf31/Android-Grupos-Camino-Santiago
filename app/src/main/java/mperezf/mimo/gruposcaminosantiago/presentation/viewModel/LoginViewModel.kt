package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.interactor.LoginInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import retrofit2.HttpException


class LoginViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val loginInteractor: LoginInteractor = LoginInteractor(app.getDataStorage(), mainThread(), Schedulers.io())

    private val errorMsg = MutableLiveData<String>()
    private val showLoading = MutableLiveData<Boolean>()
    private val finishLogin = MutableLiveData<Boolean>()


    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }


    fun getFinishLogin(): LiveData<Boolean> {
        return finishLogin
    }


    fun doLogin(email: String, password: String) {

        showLoading.postValue(true)

        loginInteractor.execute(object : DisposableObserver<User>() {
            override fun onNext(user: User) {
                finishLogin.postValue(true)
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)

                if (e is HttpException && e.code() == 404) {
                    errorMsg.postValue(application.getString(R.string.user_or_pass_not_valid))
                } else {
                    errorMsg.postValue(application.getString(R.string.internet_error))
                }
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, User(email = email, password = password))
    }


    override fun dispose() {
        super.dispose()
        loginInteractor.dispose()
    }

    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(application) as T
        }
    }
}
