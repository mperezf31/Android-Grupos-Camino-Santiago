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
import mperezf.mimo.gruposcaminosantiago.domain.interactor.GroupDetailnteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.SendMessageInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.MessageGroup

class GroupChatViewModel(app: CaminoDeSantiagoApp) : BaseViewModel(application = app) {

    private val showRefresh = MutableLiveData<Boolean>()
    private val sendingState = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()
    private val updateMessages = MutableLiveData<Group>()

    private val sendMessageInteractor: SendMessageInteractor =
        SendMessageInteractor(
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

    fun getRefreshState(): LiveData<Boolean> {
        return showRefresh
    }

    fun getSendingState(): LiveData<Boolean> {
        return sendingState
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getUpdateMessage(): LiveData<Group> {
        return updateMessages
    }

    fun sendMessage(idGroup: Int, msg: String) {
        sendingState.postValue(true)

        sendMessageInteractor.execute(
            object : DisposableObserver<Group>() {

                override fun onError(e: Throwable) {
                    sendingState.postValue(false)
                    errorMsg.postValue(application.getString(R.string.internet_error))
                }

                override fun onNext(group: Group) {
                    updateMessages.postValue(group)
                }

                override fun onComplete() {
                    sendingState.postValue(false)
                }

            },
            MessageGroup(
                idGroup = idGroup,
                message = Message(content = msg, whenSent = System.currentTimeMillis() / 1000)
            )
        )
    }

    fun getGroupDetail(groupId: Int) {
        showRefresh.postValue(true)

        groupDetailnteractor.execute(object : DisposableObserver<Group>() {

            override fun onError(e: Throwable) {
                showRefresh.postValue(false)
                errorMsg.postValue(application.getString(R.string.internet_error))
            }

            override fun onNext(t: Group) {
                updateMessages.postValue(t)
            }

            override fun onComplete() {
                showRefresh.postValue(false)
            }

        }, groupId)
    }

    override fun dispose() {
        super.dispose()
        sendMessageInteractor.dispose()
        groupDetailnteractor.dispose()
    }

    class Factory(private val application: CaminoDeSantiagoApp) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GroupChatViewModel(application) as T
        }
    }

}
