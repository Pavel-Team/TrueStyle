package ru.dm.android.truestyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dm.android.truestyle.database.dao.UserDao
import ru.dm.android.truestyle.database.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class TrueStyleDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}