package mperezf.mimo.gruposcaminosantiago.data

import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.data.mapper.UserMapper
import mperezf.mimo.gruposcaminosantiago.data.network.ApiService
import mperezf.mimo.gruposcaminosantiago.domain.model.*
import okhttp3.ResponseBody

class ApplicationStorage (private var mApiService: ApiService):DataStorege {

    override fun login(user: UserData): Observable<User> {
        return mApiService.login(user).map(UserMapper().getMapper())
    }

    override fun register(user: UserData): Observable<User> {
        return mApiService.register(user).map(UserMapper().getMapper())
    }

    override fun getGroups(): Observable<List<Group>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupDetail(id: Int): Observable<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addGroup(group: GroupData): Observable<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteGroup(): Observable<ResponseBody> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMemberGroup(groupId: Int): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMemberGroup(groupId: Int): Observable<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMessageGroup(groupId: Int, message: MessageData): Observable<Group> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}