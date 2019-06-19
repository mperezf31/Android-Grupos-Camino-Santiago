package mperezf.mimo.gruposcaminosantiago.data.local

import io.reactivex.Completable
import io.reactivex.Maybe
import mperezf.mimo.gruposcaminosantiago.data.model.UserData

interface DataPersistence {

    fun saveAuthenticatedUser(user: UserData)

    fun getAuthenticatedUser(): Maybe<UserData>

    fun logout(): Completable
}