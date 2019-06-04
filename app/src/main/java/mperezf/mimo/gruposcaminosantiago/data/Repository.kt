package mperezf.mimo.gruposcaminosantiago.data

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.BuildConfig
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp
import mperezf.mimo.gruposcaminosantiago.data.local.LocalStorage
import mperezf.mimo.gruposcaminosantiago.data.mapper.GroupMapper
import mperezf.mimo.gruposcaminosantiago.data.mapper.MessageMapper
import mperezf.mimo.gruposcaminosantiago.data.mapper.UserMapper
import mperezf.mimo.gruposcaminosantiago.data.remote.ApiService
import mperezf.mimo.gruposcaminosantiago.data.remote.RetrofitClient
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import okhttp3.ResponseBody

object Repository : DataStorage {

    private val apiService: ApiService = RetrofitClient(BuildConfig.SERVER_URL).getApiService()
    private val localStorage: LocalStorage = LocalStorage(CaminoDeSantiagoApp.instance)

    //Mappers
    private val userMapper = UserMapper()
    private val messageMapper = MessageMapper()
    private val groupMapper = GroupMapper()

    override fun getAuthenticatedUser(): Maybe<User> {
        return localStorage.getAuthenticatedUser().map(userMapper.getMapper())
    }

    override fun logout(): Completable {
        return localStorage.logout()
    }

    override fun login(user: User): Observable<User> {
        return apiService.login(userMapper.reverseMap(user)).doOnNext {
            localStorage.saveAuthenticatedUser(it)
        }.map(userMapper.getMapper())
    }

    override fun register(user: User): Observable<User> {
        return apiService.register(userMapper.reverseMap(user)).map(userMapper.getMapper())
    }

    override fun getGroups(): Observable<List<Group>> {
        return apiService.getGroups(getAuthenticationToken()).map(groupMapper.getListMapper())
    }

    override fun getGroupDetail(id: Int): Observable<Group> {
        return apiService.getGroupDetail(getAuthenticationToken(), id).map(groupMapper.getMapper())
    }

    override fun addGroup(group: Group): Observable<Group> {
        return apiService.addGroup(getAuthenticationToken(), groupMapper.reverseMap(group)).map(groupMapper.getMapper())
    }

    override fun deleteGroup(): Observable<ResponseBody> {
        return apiService.deleteGroup(getAuthenticationToken())
    }

    override fun addMemberGroup(groupId: Int): Observable<Group> {
        return apiService.addMemberGroup(getAuthenticationToken(), groupId).map(groupMapper.getMapper())
    }

    override fun removeMemberGroup(groupId: Int): Observable<Group> {
        return apiService.removeMemberGroup(getAuthenticationToken(), groupId).map(groupMapper.getMapper())
    }

    override fun addMessageGroup(groupId: Int, message: Message): Observable<Group> {
        return apiService.addMessageGroup(getAuthenticationToken(), groupId, messageMapper.reverseMap(message))
            .map(groupMapper.getMapper())
    }

    private fun getAuthenticationToken(token: String = ""): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()
        headers.put("Authentication", token)
        return headers
    }
}