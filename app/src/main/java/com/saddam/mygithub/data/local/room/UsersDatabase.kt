package com.saddam.mygithub.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saddam.mygithub.data.local.entity.Users

@Database(entities = [Users::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var instance: UsersDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UsersDatabase {
            if (instance == null){
                synchronized(UsersDatabase::class.java){
                    instance = Room.databaseBuilder(context.applicationContext,
                        UsersDatabase::class.java, "User.db")
                        .build()
                }
            }
            return instance as UsersDatabase
        }
    }
}