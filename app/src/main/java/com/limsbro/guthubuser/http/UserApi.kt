package com.limsbro.guthubuser.http

import com.limsbro.guthubuser.model.UserModel
import com.limsbro.guthubuser.model.UserProfileModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
interface UserApi {

    @GET("users?since={lastId}")
    suspend fun getUserList(@Path("lastId") lastId: String?) : Call<List<UserModel>>

    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String?): Call<UserProfileModel?>?

}