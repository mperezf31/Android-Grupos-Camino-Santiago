package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.LogoutInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User


class MainViewModel : BaseViewModel() {

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            Repository,
            mainThread(),
            Schedulers.io()
        )

    private val logoutInteractor: LogoutInteractor =
        LogoutInteractor(Repository, mainThread(), Schedulers.io())


    fun getAuthenticatedUser(authenticatedUser: (User) -> Unit, goToLogin: () -> Unit) {

        authenticatedUserInteractor.execute(object : DisposableMaybeObserver<User>() {

            override fun onError(e: Throwable) { }

            override fun onSuccess(user: User) {
                authenticatedUser(user)
            }

            override fun onComplete() {
                goToLogin()
            }

        }, Unit)
    }


  fun logout(callback: () -> Unit) {
      logoutInteractor.execute(object : DisposableCompletableObserver() {

          override fun onError(e: Throwable) { }

          override fun onComplete() {
              callback.invoke()
          }

        }, Unit)
    }


    override fun dispose() {
        authenticatedUserInteractor.dispose()
    }
}
