package mperezf.mimo.gruposcaminosantiago.data

import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.domain.model.*
import okhttp3.ResponseBody

interface DataStorege {

    fun login(user: UserData): Observable<User>

    fun register(user: UserData): Observable<User>

    fun getGroups(): Observable<List<Group>>

    fun getGroupDetail(id: Int): Observable<Group>

    fun addGroup(group: GroupData): Observable<Group>

    fun deleteGroup(): Observable<ResponseBody>

    fun addMemberGroup(groupId: Int): Observable<User>

    fun removeMemberGroup(groupId: Int): Observable<User>

    fun addMessageGroup(groupId: Int, message: MessageData): Observable<Group>
}