package mperezf.mimo.gruposcaminosantiago.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import mperezf.mimo.gruposcaminosantiago.R
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.domain.interactor.AuthenticatedUserInteractor
import mperezf.mimo.gruposcaminosantiago.domain.interactor.SendMessageInteractor
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.MessageGroup
import mperezf.mimo.gruposcaminosantiago.domain.model.User

class GroupChatViewModel : BaseViewModel() {

    private val showLoading = MutableLiveData<Boolean>()
    private val errorMsg = MutableLiveData<String>()
    private val messageSended = MutableLiveData<Group>()

    private val authenticatedUserInteractor: AuthenticatedUserInteractor =
        AuthenticatedUserInteractor(
            Repository,
            mainThread(),
            Schedulers.io()
        )

    private val sendMessageInteractor: SendMessageInteractor =
        SendMessageInteractor(
            Repository,
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


    fun getAuthenticatedUser(authenticatedUser: (User) -> Unit) {
        authenticatedUserInteractor.execute(object : DisposableMaybeObserver<User>() {

            override fun onError(e: Throwable) {}

            override fun onSuccess(user: User) {
                authenticatedUser(user)
            }

            override fun onComplete() {}

        }, Unit)
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
        authenticatedUserInteractor.dispose()
        sendMessageInteractor.dispose()
    }

}
