package com.saddam.mygithub.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saddam.mygithub.data.UsersRepository
import com.saddam.mygithub.data.local.entity.Users
import com.saddam.mygithub.data.remote.response.UserDetail
import com.saddam.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val usersRepository: UsersRepository = UsersRepository(application)

    fun insert(users: Users){
        usersRepository.insert(users)
    }

    fun delete(users: Users){
        usersRepository.delete(users)
    }

    fun getAllUser(): LiveData<List<Users>> = usersRepository.getAllUsers()

    private val _users = MutableLiveData<UserDetail?>()
    val users: LiveData<UserDetail?> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    _isLoading.value = false
                    _users.value = responseBody
                }else{
                    Log.e(TAG, "onResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })
    }

    fun setDataUser(data: UserDetail?): Users {
        return Users(
            data?.username.toString(),
            data?.name.toString(),
            data?.avatarUrl,
            data?.followers.toString(),
            data?.following.toString()
            )
    }

}