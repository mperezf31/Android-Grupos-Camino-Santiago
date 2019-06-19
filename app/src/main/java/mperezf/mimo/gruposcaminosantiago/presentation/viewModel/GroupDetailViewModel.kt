package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.GroupDetailnteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RemoveGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import okhttp3.ResponseBody


class GroupDetailViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    val showLoading = MutableLiveData<Boolean>()

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            app.getDataStorage(),
            mainThread(),
            Schedulers.io()
        )

    private val groupDetailnteractor: GroupDetailnteractor =
        GroupDetailnteractor(
            app.getDataStorage(),
            mainThread(),
            Schedulers.io()
        )

    private val removeGrouplnteractor: RemoveGrouplnteractor =
        RemoveGrouplnteractor(
            app.getDataStorage(),
            mainThread(),
            Schedulers.io()
        )


    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getGroupDetail(groupId: Int, success: (Group) -> Unit, error: (String) -> Unit) {

        showLoading.postValue(true)

        groupDetailnteractor.execute(object : DisposableObserver<Group>() {

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                error(application.getString(R.string.internet_error))

            }

            override fun onNext(t: Group) {
                success(t)
            }

            override fun onComplete() {
                showLoading.postValue(false)
            }

        }, groupId)
    }


    fun deleteGroup(groupId: Int, success: () -> Unit, error: (String) -> Unit) {

        showLoading.postValue(true)

        removeGrouplnteractor.execute(object : DisposableObserver<ResponseBody>() {
            override fun onNext(t: ResponseBody) {
                success.invoke()
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                error(application.getString(R.string.internet_error))

            }

            override fun onComplete() {
                showLoading.postValue(false)
            }

        }, groupId)

    }

    override fun dispose() {
        authenticatedUserInteractor.dispose()
        groupDetailnteractor.dispose()
        removeGrouplnteractor.dispose()
    }


    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupDetailViewModel(application) as T
        }
    }

}
