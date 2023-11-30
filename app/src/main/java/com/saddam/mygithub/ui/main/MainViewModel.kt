package com.saddam.mygithub.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.saddam.mygithub.data.remote.response.DetailsItem
import com.saddam.mygithub.data.remote.response.UserResponse
import com.saddam.mygithub.ui.setting.SettingPreferences
import com.saddam.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _users = MutableLiveData<List<DetailsItem>>()
    val users: LiveData<List<DetailsItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllUser()
    }

    fun getUserBySearch(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserByUsername(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getAllUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<ArrayList<DetailsItem>> {
            override fun onResponse(
                call: Call<ArrayList<DetailsItem>>,
                response: Response<ArrayList<DetailsItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _users.value = responseBody!!
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<DetailsItem>>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}