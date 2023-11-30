package com.saddam.mygithub.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saddam.mygithub.data.local.entity.Users

@Dao
interface UsersDao {

    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getUsers(): LiveData<List<Users>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(users: Users)

    @Delete
    fun delete(users: Users)

}