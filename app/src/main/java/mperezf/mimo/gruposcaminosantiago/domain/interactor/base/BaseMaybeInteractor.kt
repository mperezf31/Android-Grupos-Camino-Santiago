package mperezf.mimo.gruposcaminosantiago.domain.interactor.base

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver

abstract class BaseMaybeInteractor<T, P>(var mainThread: Scheduler, var iOExecutor: Scheduler) {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun result(params: P): Maybe<T>

    fun execute(observer: DisposableMaybeObserver<T>, params: P) {
        val observable = result(params)
            .subscribeOn(iOExecutor)
            .observeOn(mainThread)
        mCompositeDisposable.add(observable.subscribeWith(observer))
    }


    fun dispose() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}