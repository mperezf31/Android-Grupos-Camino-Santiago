package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseCompletableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User


class LogoutInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseCompletableInteractor<User, Unit>(mainThread, iOExecutor) {

    override fun result(params:Unit): Completable{
        return repository.logout()
    }
}