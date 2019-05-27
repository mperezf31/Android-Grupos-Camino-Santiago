package mperezf.mimo.gruposcaminosantiago.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import mperezf.mimo.gruposcaminosantiago.data.local.db.AppDatabase
import mperezf.mimo.gruposcaminosantiago.data.model.UserData


class LocalStorage(context: Context) {

    val db: AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "database-grupos-camino-santiago"
    ).build()

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveAuthenticatedUser(user: UserData): Completable{
        return db.userDao().saveUser(user)
    }

    fun getAuthenticatedUser(): Observable<UserData> {
        return db.userDao().getAuthenticatedUser
    }

    fun logout(){
        return db.userDao().deleteAllUser()
    }

}

