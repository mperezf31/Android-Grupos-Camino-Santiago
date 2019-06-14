package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.GroupDetailnteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RemoveGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import okhttp3.ResponseBody


class GroupDetailViewModel : BaseViewModel() {

    val showLoading = MutableLiveData<Boolean>()

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            Repository,
            mainThread(),
            Schedulers.io()
        )

    private val groupDetailnteractor: GroupDetailnteractor =
        GroupDetailnteractor(
            Repository,
            mainThread(),
            Schedulers.io()
        )

    private val removeGrouplnteractor: RemoveGrouplnteractor =
        RemoveGrouplnteractor(
            Repository,
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
                error(context.getString(R.string.internet_error))

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
                error(context.getString(R.string.internet_error))

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


}
