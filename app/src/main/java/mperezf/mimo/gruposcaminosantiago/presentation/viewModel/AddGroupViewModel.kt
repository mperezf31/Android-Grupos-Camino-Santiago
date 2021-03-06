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
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AddGroupInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group

class AddGroupViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val addGroupInteractor: AddGroupInteractor =
        AddGroupInteractor(app.getDataStorage(), mainThread(), Schedulers.io())

    private val errorMsg = MutableLiveData<String>()
    private val showLoading = MutableLiveData<Boolean>()
    private val groupAddSuccess = MutableLiveData<Group>()

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getGroupAdd(): LiveData<Group> {
        return groupAddSuccess
    }

    fun createGroup(newGroup: Group) {

        showLoading.postValue(true)

        addGroupInteractor.execute(object : DisposableObserver<Group>() {
            override fun onNext(group: Group) {
                groupAddSuccess.postValue(group)
            }

            override fun onError(e: Throwable) {
                showLoading.postValue(false)
                errorMsg.postValue(application.getString(R.string.internet_error))
            }

            override fun onComplete() {
                showLoading.postValue(false)

            }
        }, newGroup)
    }

    override fun dispose() {
        addGroupInteractor.dispose()
    }


    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddGroupViewModel(application) as T
        }
    }

}
