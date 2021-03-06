package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.domain.model.UserGroupList


class GroupListInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseObservableInteractor<UserGroupList, Unit>(mainThread, iOExecutor) {

    override fun result(params: Unit): Observable<UserGroupList> {
        return repository.getGroups()
    }
}