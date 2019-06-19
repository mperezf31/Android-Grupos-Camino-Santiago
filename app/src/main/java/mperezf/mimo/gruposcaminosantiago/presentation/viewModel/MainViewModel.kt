package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.domain.interactor.LogoutInteractor


class MainViewModel : BaseViewModel() {

    private val logoutInteractor: LogoutInteractor =
        LogoutInteractor(CaminoDeSantiagoApp.instance.getDataStorage(), mainThread(), Schedulers.io())


    fun logout(callback: () -> Unit) {
        logoutInteractor.execute(object : DisposableCompletableObserver() {

            override fun onError(e: Throwable) {}

            override fun onComplete() {
                callback.invoke()
            }

        }, Unit)
    }

}
