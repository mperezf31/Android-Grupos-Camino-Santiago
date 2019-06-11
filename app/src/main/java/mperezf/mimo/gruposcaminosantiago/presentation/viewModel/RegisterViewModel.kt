package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RegisterInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import retrofit2.HttpException

class RegisterViewModel : BaseViewModel() {


    private val registerInteractor: RegisterInteractor =
        RegisterInteractor(Repository, AndroidSchedulers.mainThread(), Schedulers.io())

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
                    errorMsg.postValue(context.getString(R.string.user_or_pass_not_valid))
                } else {
                    errorMsg.postValue(context.getString(R.string.internet_error))
                }
                errorMsg.postValue(context.getString(R.string.user_or_pass_not_valid))
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, newUser)
    }


    override fun dispose() {
        registerInteractor.dispose()
    }

}
