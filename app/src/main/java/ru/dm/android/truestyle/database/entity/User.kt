package ru.dm.android.truestyle.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@PrimaryKey val id: Long,
                val token: String,
                val type: String,
                val username: String = "") {
}