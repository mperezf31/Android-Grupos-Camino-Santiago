package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.interactor.base.BaseObservableInteractor
import okhttp3.ResponseBody


class RemoveGrouplnteractor constructor(
    private val repository: DataStorage,
    mainThread: Scheduler,
    iOExecutor: Scheduler
) : BaseObservableInteractor<ResponseBody, Int>(mainThread, iOExecutor) {

    override fun result(params: Int): Observable<ResponseBody> {
        return repository.deleteGroup(params)
    }
}