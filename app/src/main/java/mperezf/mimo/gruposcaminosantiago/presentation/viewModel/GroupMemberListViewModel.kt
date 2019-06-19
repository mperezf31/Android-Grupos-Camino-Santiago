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
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AddMemberGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.RemoveMemberGrouplnteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.presentation.model.Member

class GroupMemberListViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val showLoading = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()
    private val memberAddedToGroup = MutableLiveData<Group>()
    private val memberRemovedFromGroup = MutableLiveData<Group>()


    private val addMemberGrouplnteractor: AddMemberGrouplnteractor =
        AddMemberGrouplnteractor(app.getDataStorage(), mainThread(), Schedulers.io())

    private val removeMemberGrouplnteractor: RemoveMemberGrouplnteractor =
        RemoveMemberGrouplnteractor(
            app.getDataStorage(),
            mainThread(), Schedulers.io()
        )


    fun addMemberToGroup(groupId: Int) {
        showLoading.postValue(true)
        addMemberGrouplnteractor.execute(object : DisposableObserver<Group>() {

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                errorMsg.postValue(application.getString(R.string.internet_error))
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
                errorMsg.postValue(application.getString(R.string.internet_error))
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

    fun getGroupMembers(group: Group): ArrayList<Member> {
        return if (group.members != null) {
            val members = ArrayList<Member>()
            members.add(Member(group.founder?.id, group.founder?.name, group.founder?.photo, true))

            members.addAll(group.members.map {
                Member(it.id, it.name, it.photo, false)
            })

            ArrayList(members)
        } else {
            ArrayList()
        }

    }


    override fun dispose() {
        super.dispose()
        addMemberGrouplnteractor.dispose()
        removeMemberGrouplnteractor.dispose()
    }

    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupMemberListViewModel(application) as T
        }
    }


}
