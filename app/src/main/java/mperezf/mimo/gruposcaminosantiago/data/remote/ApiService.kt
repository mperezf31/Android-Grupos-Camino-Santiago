package mperezf.mimo.gruposcaminosantiago.data.remote

import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.data.model.GroupData
import mperezf.mimo.gruposcaminosantiago.data.model.MessageData
import mperezf.mimo.gruposcaminosantiago.data.model.UserData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body user: UserData): Observable<UserData>

    @POST("user")
    fun register(@Body user: UserData): Observable<UserData>

    @GET
    fun getGroups(@HeaderMap token: Map<String, String>): Observable<List<GroupData>>

    @GET("group/{id}")
    fun getGroupDetail(@HeaderMap token: Map<String, String>, @Path("id") id: Int): Observable<GroupData>

    @POST("group")
    fun addGroup(@HeaderMap token: Map<String, String>, group: GroupData): Observable<GroupData>

    @DELETE("group/{id}")
    fun deleteGroup(@HeaderMap token: Map<String, String>): Observable<ResponseBody>

    @POST("group/{id}/pilgrim")
    fun addMemberGroup(@HeaderMap token: Map<String, String>, @Path("id") groupId: Int): Observable<GroupData>

    @DELETE("group/{id}/pilgrim")
    fun removeMemberGroup(@HeaderMap token: Map<String, String>, @Path("id") groupId: Int): Observable<GroupData>

    @POST("group/{id}/post")
    fun addMessageGroup(@HeaderMap token: Map<String, String>, @Path("id") groupId: Int, @Body message: MessageData): Observable<GroupData>

}