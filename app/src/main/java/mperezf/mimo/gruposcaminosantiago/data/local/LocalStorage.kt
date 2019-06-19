package mperezf.mimo.gruposcaminosantiago.data.local

import android.content.Context
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Maybe
import mperezf.mimo.gruposcaminosantiago.data.local.db.AppDatabase
import mperezf.mimo.gruposcaminosantiago.data.model.UserData


class LocalStorage(context: Context) : DataPersistence {

    private val db: AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "database-grupos-camino-santiago"
    ).build()

    override fun saveAuthenticatedUser(user: UserData) {
        return db.userDao().saveUser(user)
    }

    override fun getAuthenticatedUser(): Maybe<UserData> {
        return db.userDao().getAuthenticatedUser
    }

    override fun logout(): Completable {
        return Completable.fromAction {
            db.userDao().deleteAllUser()
        }
    }

}

