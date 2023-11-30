package com.saddam.mygithub.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.saddam.mygithub.data.UsersRepository
import com.saddam.mygithub.data.local.entity.Users

class FavoriteViewModel(application: Application): ViewModel() {

    private val usersRepository: UsersRepository = UsersRepository(application)

    fun getAllUser(): LiveData<List<Users>> = usersRepository.getAllUsers()
}