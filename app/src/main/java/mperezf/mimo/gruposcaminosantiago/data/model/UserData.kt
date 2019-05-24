package mperezf.mimo.gruposcaminosantiago.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(

    @PrimaryKey val id: Int,
    val email: String,
    val name: String,
    val photo: String
)