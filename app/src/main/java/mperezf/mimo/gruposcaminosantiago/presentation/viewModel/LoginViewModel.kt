package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.LoginInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import retrofit2.HttpException


class LoginViewModel : BaseViewModel() {

    private val loginInteractor: LoginInteractor = LoginInteractor(Repository, mainThread(), Schedulers.io())

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

                if ((e as HttpException).code() == 404) {
                    errorMsg.postValue(context.getString(R.string.user_or_pass_not_valid))
                } else {
                    errorMsg.postValue(context.getString(R.string.internet_error))
                }
                errorMsg.postValue(context.getString(R.string.user_or_pass_not_valid))
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, User(email = email, password = password))
    }


    override fun dispose() {
        loginInteractor.dispose()
    }
}
