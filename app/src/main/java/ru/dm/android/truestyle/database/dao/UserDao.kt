package ru.dm.android.truestyle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.dm.android.truestyle.database.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<User>>

    @Insert
    fun addUser(user: User)

    @Query("DELETE FROM user WHERE id = (:id)")
    fun deleteUser(id: Long)
}