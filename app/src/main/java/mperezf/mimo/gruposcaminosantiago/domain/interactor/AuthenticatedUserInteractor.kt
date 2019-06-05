package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Maybe
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseMaybeInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User


class AuthenticatedUserInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseMaybeInteractor<User, Unit>(mainThread, iOExecutor) {

    override fun result(params: Unit): Maybe<User> {
        return repository.getAuthenticatedUser()
    }
}