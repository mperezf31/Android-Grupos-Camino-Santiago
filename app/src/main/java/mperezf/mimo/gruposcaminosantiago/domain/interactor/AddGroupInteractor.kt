package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User


class AddGroupInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseObservableInteractor<Group, Group>(mainThread, iOExecutor) {

    override fun result(params: Group): Observable<Group> {
        return repository.addGroup(params)
    }
}