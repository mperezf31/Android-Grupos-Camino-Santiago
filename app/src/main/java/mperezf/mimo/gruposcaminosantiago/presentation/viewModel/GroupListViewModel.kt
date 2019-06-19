package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.interactor.GroupListInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.UserGroupList
import mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment.SettingsFragment


class GroupListViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {


    private val groupListInteractor: GroupListInteractor =
        GroupListInteractor(
            app.getDataStorage(),
            AndroidSchedulers.mainThread(),
            Schedulers.io()
        )

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


    fun getGroups(userLocation: Location? = null) {

        showLoading.postValue(true)

        groupListInteractor.execute(object : DisposableObserver<UserGroupList>() {
            override fun onNext(groupList: UserGroupList) {
                showGroups(userLocation, groupList)
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                errorMsg.postValue(application.getString(R.string.internet_error))
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, Unit)
    }

    private fun showGroups(userLocation: Location?, groupList: UserGroupList) {
        var groups = ArrayList<Group>()

        if (preferences.getBoolean(SettingsFragment.PREF_GROUPS_CREATED, true)) {
            groups.addAll(groupList.groupsCreated)
        }

        if (preferences.getBoolean(SettingsFragment.PREF_GROUPS_MEMBER, true)) {
            groups.addAll(groupList.groupsAssociated)
        }

        if (preferences.getBoolean(SettingsFragment.PREF__OTHER_GROUPS, true)) {
            groups.addAll(groupList.otherGroups)
        }

        //Calculate group distance
        userLocation?.let { userCoordinates ->
            groups = ArrayList(groups.map {
                Group(
                    id = it.id,
                    photo = it.photo,
                    title = it.title,
                    description = it.description,
                    departureDate = it.departureDate,
                    arrivalDate = it.arrivalDate,
                    departurePlace = it.departurePlace,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    distance = it.latitude?.let { it1 ->
                        it.longitude?.let { it2 ->
                            getDistance(
                                userCoordinates, it1,
                                it2
                            )
                        }
                    }
                )
            })

            fun selector(p: Group): Int? = p.distance
            groups.sortBy { selector(it) }
        }

        groupsUpdate.postValue(groups)
    }


    private fun getDistance(userLocation: Location, latitude: Double, longitude: Double): Int {
        val departureLocation = Location("")
        departureLocation.latitude = latitude
        departureLocation.longitude = longitude
        return (userLocation.distanceTo(departureLocation) / 1000).toInt()
    }


    override fun dispose() {
        groupListInteractor.dispose()
    }

    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupListViewModel(application) as T
        }
    }

}
