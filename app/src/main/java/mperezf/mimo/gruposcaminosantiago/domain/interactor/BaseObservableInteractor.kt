package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

abstract class BaseObservableInteractor<T, P>(var mainThread: Scheduler, var iOExecutor: Scheduler) {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun result(params: P): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: P) {
        val observable = result(params)
            .subscribeOn(iOExecutor)
            .observeOn(mainThread)
        mCompositeDisposable.add(observable.subscribeWith(observer))
    }


    fun close() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}