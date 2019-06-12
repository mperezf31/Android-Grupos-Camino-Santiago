package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.GroupListInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.UserGroupList
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.SettingsFragment


class GroupListViewModel : BaseViewModel() {


    private val groupListInteractor: GroupListInteractor =
        GroupListInteractor(Repository, AndroidSchedulers.mainThread(), Schedulers.io())

    val errorMsg = MutableLiveData<String>()
    val showLoading = MutableLiveData<Boolean>()
    val groupsUpdate = MutableLiveData<List<Group>>()


    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getGroupsUpdate(): LiveData<List<Group>> {
        return groupsUpdate
    }


    fun getGroups() {

        showLoading.postValue(true)

        groupListInteractor.execute(object : DisposableObserver<UserGroupList>() {
            override fun onNext(groupList: UserGroupList) {
                showGroups(groupList)
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                errorMsg.postValue(context.getString(R.string.internet_error))
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, Unit)
    }

    private fun showGroups(groupList: UserGroupList) {
        val groups: ArrayList<Group> = ArrayList()

        if (preferences.getBoolean(SettingsFragment.PREF_GROUPS_CREATED, true)) {
            groups.addAll(groupList.groupsCreated)
        }

        if (preferences.getBoolean(SettingsFragment.PREF_GROUPS_MEMBER, true)) {
            groups.addAll(groupList.groupsAssociated)
        }

        if (preferences.getBoolean(SettingsFragment.PREF__OTHER_GROUPS, true)) {
            groups.addAll(groupList.otherGroups)
        }

        groupsUpdate.postValue(groups)

    }


    override fun dispose() {
        groupListInteractor.dispose()
    }

}
