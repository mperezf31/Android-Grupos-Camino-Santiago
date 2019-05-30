package mperezf.mimo.gruposcaminosantiago.domain.interactor

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver

abstract class BaseCompletableInteractor<T, P>(var mainThread: Scheduler, var iOExecutor: Scheduler) {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun result(params: P): Completable

    fun execute(observer: DisposableCompletableObserver, params: P) {
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