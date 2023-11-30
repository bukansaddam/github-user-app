package com.saddam.mygithubapp.data.retrofit

import com.saddam.mygithub.BuildConfig
import com.saddam.mygithub.data.remote.response.DetailsItem
import com.saddam.mygithub.data.remote.response.UserDetail
import com.saddam.mygithub.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    private companion object{
        const val API_KEY = BuildConfig.TOKEN
    }

    @GET("users")
    @Headers("Authorization: $API_KEY")
    fun getAllUser(): Call<ArrayList<DetailsItem>>

    @GET("search/users")
    @Headers("Authorization: $API_KEY")
    fun getUserByUsername(@Query("q") username: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: $API_KEY")
    fun getDetailUser(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: $API_KEY")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<DetailsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: $API_KEY")
    fun getFollowings(@Path("username") username: String): Call<ArrayList<DetailsItem>>


}