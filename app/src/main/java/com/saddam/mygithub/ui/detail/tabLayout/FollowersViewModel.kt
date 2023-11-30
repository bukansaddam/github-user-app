package com.saddam.mygithub.ui.detail.tabLayout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saddam.mygithub.data.remote.response.DetailsItem
import com.saddam.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel() : ViewModel() {

    companion object {
        private const val TAG = "FollowersViewModel"
    }

    private val _users = MutableLiveData<List<DetailsItem>>()
    val users: LiveData<List<DetailsItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollower(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: Callback<ArrayList<DetailsItem>> {
            override fun onResponse(
                call: Call<ArrayList<DetailsItem>>,
                response: Response<ArrayList<DetailsItem>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _isLoading.value = false
                    _users.value = responseBody!!
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<DetailsItem>>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}", )
            }

        })
    }
}