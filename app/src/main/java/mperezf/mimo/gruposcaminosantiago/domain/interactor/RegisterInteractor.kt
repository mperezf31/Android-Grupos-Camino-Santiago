package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User


class RegisterInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseObservableInteractor<User, User>(mainThread, iOExecutor) {

    override fun result(params: User): Observable<User> {
        return repository.register(params)
    }
}