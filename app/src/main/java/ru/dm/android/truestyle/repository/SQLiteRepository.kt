package ru.dm.android.truestyle.repository

import android.content.Context
import androidx.room.Room
import ru.dm.android.truestyle.api.response.Auth
import ru.dm.android.truestyle.database.TrueStyleDatabase
import ru.dm.android.truestyle.database.entity.User
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "true-style-database"

class SQLiteRepository private constructor(context: Context){

    private val database: TrueStyleDatabase = Room.databaseBuilder(
        context.applicationContext,
        TrueStyleDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val userDao =  database.userDao()
    private val executor = Executors.newSingleThreadExecutor()


    fun getUsers() = userDao.getUsers()
    fun addUser(auth: Auth) {
        executor.execute {
            userDao.addUser(User(auth.id, auth.token, auth.type, auth.username))
        }
    }
//    fun deleteUser() {
//        executor.execute {
//            userDao.deleteUser(getUser()!!.id)
//        }
//    }


    companion object {
        private var INSTANCE: SQLiteRepository? = null

        //Инициализация репозитория
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SQLiteRepository(context)
            }
        }

        //Доступ к репозиторию
        fun get(): SQLiteRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}