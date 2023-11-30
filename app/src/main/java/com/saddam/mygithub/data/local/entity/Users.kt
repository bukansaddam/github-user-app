package com.saddam.mygithub.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
class Users(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    var username: String,

    @field:ColumnInfo(name = "name")
    var names: String,

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "followers")
    var followers: String,

    @field:ColumnInfo(name = "following")
    var following: String,

) : Parcelable