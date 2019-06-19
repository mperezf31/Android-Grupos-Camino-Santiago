package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RegisterInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import retrofit2.HttpException

class RegisterViewModel (app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val registerInteractor: RegisterInteractor =
        RegisterInteractor(
            app.getDataStorage(),
            AndroidSchedulers.mainThread(),
            Schedulers.io()
        )

    private val errorMsg = MutableLiveData<String>()
    private val showLoading = MutableLiveData<Boolean>()
    private val finishRegister = MutableLiveData<Boolean>()


    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getFinishRegister(): LiveData<Boolean> {
        return finishRegister
    }

    fun register(newUser: User) {

        showLoading.postValue(true)

        registerInteractor.execute(object : DisposableObserver<User>() {
            override fun onNext(user: User) {
                finishRegister.postValue(true)
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)

                if (e is HttpException && e.code() == 404) {
                    errorMsg.postValue(application.getString(R.string.email_not_valid))
                } else {
                    errorMsg.postValue(application.getString(R.string.internet_error))
                }
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, newUser)
    }


    override fun dispose() {
        registerInteractor.dispose()
    }


    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(application) as T
        }
    }

}
