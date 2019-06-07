package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AddMemberGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RemoveMemberGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User

class GroupMemberListViewModel : BaseViewModel() {

    private val showLoading = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()
    private val memberAddedToGroup = MutableLiveData<Group>()
    private val memberRemovedFromGroup = MutableLiveData<Group>()

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            Repository,
            mainThread(),
            Schedulers.io()
        )

    private val addMemberGrouplnteractor: AddMemberGrouplnteractor =
        AddMemberGrouplnteractor(Repository, mainThread(), Schedulers.io())

    private val removeMemberGrouplnteractor: RemoveMemberGrouplnteractor =
        RemoveMemberGrouplnteractor(Repository, mainThread(), Schedulers.io())


    fun getAuthenticatedUser(authenticatedUser: (User) -> Unit) {
        authenticatedUserInteractor.execute(object : DisposableMaybeObserver<User>() {

            override fun onError(e: Throwable) {}

            override fun onSuccess(user: User) {
                authenticatedUser(user)
            }

            override fun onComplete() {}

        }, Unit)
    }

    fun addMemberToGroup(groupId: Int) {
        showLoading.postValue(true)
        addMemberGrouplnteractor.execute(object : DisposableObserver<Group>() {

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                error(context.getString(R.string.internet_error))
            }

            override fun onNext(group: Group) {
                memberAddedToGroup.postValue(group)
            }

            override fun onComplete() {
                showLoading.postValue(false)
            }

        }, groupId)
    }

    fun removeMemberGroup(groupId: Int) {
        showLoading.postValue(true)

        removeMemberGrouplnteractor.execute(object : DisposableObserver<Group>() {

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                error(context.getString(R.string.internet_error))
            }

            override fun onNext(group: Group) {
                memberRemovedFromGroup.postValue(group)
            }

            override fun onComplete() {
                showLoading.postValue(false)
            }

        }, groupId)
    }


    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getMemberWasAdded(): LiveData<Group> {
        return memberAddedToGroup
    }

    fun getMemberWasRemoved(): LiveData<Group> {
        return memberRemovedFromGroup
    }

    fun checkIsMember(userId: Int, members: List<User>): Boolean {
        return members.any {
           it.id == userId
        }
    }

    override fun dispose() {
        addMemberGrouplnteractor.dispose()
    }


}
