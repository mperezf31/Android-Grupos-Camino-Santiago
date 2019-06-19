package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.domain.interactor.LogoutInteractor


class MainViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val logoutInteractor: LogoutInteractor =
        LogoutInteractor(app.getDataStorage(), mainThread(), Schedulers.io())


    fun logout(callback: () -> Unit) {
        logoutInteractor.execute(object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {}

            override fun onComplete() {
                callback.invoke()
            }

        }, Unit)
    }

    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }

}
