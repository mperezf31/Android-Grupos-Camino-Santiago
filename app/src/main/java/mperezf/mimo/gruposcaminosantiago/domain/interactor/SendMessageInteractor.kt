package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.MessageGroup


class SendMessageInteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseObservableInteractor<Group, MessageGroup>(mainThread, iOExecutor) {

    override fun result(params: MessageGroup): Observable<Group> {
        return repository.addMessageGroup(params.idGroup, params.message)
    }
}