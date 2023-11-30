package com.saddam.mygithub.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.saddam.mygithub.data.local.entity.Users
import com.saddam.mygithub.data.local.room.UsersDao
import com.saddam.mygithub.data.local.room.UsersDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UsersRepository(application: Application) {
    private val userDao: UsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersDatabase.getDatabase(application)
        userDao = db.usersDao()
    }

    fun getAllUsers(): LiveData<List<Users>> = userDao.getUsers()

    fun insert(user: Users){
        executorService.execute{ userDao.insert(user) }
    }

    fun delete(user: Users){
        executorService.execute{ userDao.delete(user) }
    }
}