package mperezf.mimo.gruposcaminosantiago.data.local.db

import io.reactivex.Observable
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Maybe
import mperezf.mimo.gruposcaminosantiago.data.model.UserData

@Dao
interface UserModelDao {

    @get:Query("SELECT * FROM UserData LIMIT 1")
    val getAuthenticatedUser: Maybe<UserData>

    @Insert(onConflict = REPLACE)
    fun saveUser(userEntity: UserData)

    @Query("DELETE FROM UserData")
    fun deleteAllUser()
}