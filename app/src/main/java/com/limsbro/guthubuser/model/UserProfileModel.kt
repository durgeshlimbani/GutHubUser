package com.limsbro.guthubuser.model

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
data class UserProfileModel(
    val login: String = "",
    val name: String = "",
    val id: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val avatar_url: String,
    val blog: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val company: String? = null,
    val location: String? = null
)