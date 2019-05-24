package mperezf.mimo.gruposcaminosantiago.domain

import io.reactivex.Maybe
import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import okhttp3.ResponseBody

interface DataStorage {

    fun getAuthenticatedUser(): Maybe<User>

    fun login(user: User): Observable<User>

    fun register(user: User): Observable<User>

    fun getGroups(): Observable<List<Group>>

    fun getGroupDetail(id: Int): Observable<Group>

    fun addGroup(group: Group): Observable<Group>

    fun deleteGroup(): Observable<ResponseBody>

    fun addMemberGroup(groupId: Int): Observable<Group>

    fun removeMemberGroup(groupId: Int): Observable<Group>

    fun addMessageGroup(groupId: Int, message: Message): Observable<Group>
}