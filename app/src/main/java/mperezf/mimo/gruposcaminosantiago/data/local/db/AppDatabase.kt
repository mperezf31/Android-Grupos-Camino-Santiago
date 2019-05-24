package mperezf.mimo.gruposcaminosantiago.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import mperezf.mimo.gruposcaminosantiago.data.model.UserData

@Database(entities = [UserData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserModelDao
}