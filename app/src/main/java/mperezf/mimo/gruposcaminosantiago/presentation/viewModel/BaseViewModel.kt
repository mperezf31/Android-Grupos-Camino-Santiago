package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User


open class BaseViewModel(val application: CaminoDeSantiagoApp) : ViewModel() {

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            application.getDataStorage(),
            AndroidSchedulers.mainThread(),
            Schedulers.io()
        )

    fun getAuthenticatedUser(success: (User) -> Unit, error: () -> Unit) {
        authenticatedUserInteractor.execute(object : DisposableMaybeObserver<User>() {

            override fun onError(e: Throwable) {}

            override fun onSuccess(user: User) {
                success(user)
            }

            override fun onComplete() {
                error.invoke()
            }

        }, Unit)
    }


    open fun dispose() {
        authenticatedUserInteractor.dispose()
    }

}
