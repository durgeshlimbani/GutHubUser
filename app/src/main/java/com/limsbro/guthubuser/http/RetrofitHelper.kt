package com.limsbro.guthubuser.http

import com.limsbro.guthubuser.model.UserModel
import com.limsbro.guthubuser.model.UserProfileModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
interface RetrofitHelper {


    @GET("users")
    fun getUserList(@Query("since") lastId: String?) : Call<List<UserModel>>

    @GET("users/{username}")
    fun getUserProfile(@Path("username") username: String?): Call<UserProfileModel>

    companion object {
        var retrofitService: RetrofitHelper? = null
        fun getInstance() : RetrofitHelper {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitHelper::class.java)
            }
            return retrofitService!!
        }
    }
}