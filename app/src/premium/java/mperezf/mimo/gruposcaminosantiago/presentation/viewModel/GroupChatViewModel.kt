package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.domain.interactor.SendMessageInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.MessageGroup

class GroupChatViewModel : BaseViewModel() {

    private val showLoading = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()
    private val messageSended = MutableLiveData<Group>()

    private val sendMessageInteractor: SendMessageInteractor =
        SendMessageInteractor(
            CaminoDeSantiagoApp.instance.getDataStorage(),
            mainThread(),
            Schedulers.io()
        )

    fun getLoadingState(): LiveData<Boolean> {
        return showLoading
    }

    fun getErrorMsg(): LiveData<String> {
        return errorMsg
    }

    fun getSendMessage(): LiveData<Group> {
        return messageSended
    }

    fun sendMessage(idGroup: Int, msg: String) {
        showLoading.postValue(true)

        sendMessageInteractor.execute(
            object : DisposableObserver<Group>() {

                override fun onError(e: Throwable) {
                    showLoading.postValue(false)
                    errorMsg.postValue(context.getString(R.string.internet_error))
                }

                override fun onNext(group: Group) {
                    messageSended.postValue(group)
                }

                override fun onComplete() {
                    showLoading.postValue(false)
                }

            },
            MessageGroup(
                idGroup = idGroup,
                message = Message(content = msg, whenSent = System.currentTimeMillis() / 1000)
            )
        )
    }


    override fun dispose() {
        super.dispose()
        sendMessageInteractor.dispose()
    }

}
