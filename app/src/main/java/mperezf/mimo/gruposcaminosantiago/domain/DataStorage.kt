package mperezf.mimo.gruposcaminosantiago.domain

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.domain.model.Group
import mperezf.mimo.gruposcaminosantiago.domain.model.Message
import mperezf.mimo.gruposcaminosantiago.domain.model.User
import mperezf.mimo.gruposcaminosantiago.domain.model.UserGroupList
import okhttp3.ResponseBody

interface DataStorage {

    fun getAuthenticatedUser(): Maybe<User>

    fun logout(): Completable

    fun login(user: User): Observable<User>

    fun register(user: User): Observable<User>

    fun getGroups(): Observable<UserGroupList>

    fun getGroupDetail(id: Int): Observable<Group>

    fun addGroup(group: Group): Observable<Group>

    fun deleteGroup(groupId: Int): Observable<ResponseBody>

    fun addMemberGroup(groupId: Int): Observable<Group>

    fun removeMemberGroup(groupId: Int): Observable<Group>

    fun addMessageGroup(groupId: Int, message: Message): Observable<Group>
}