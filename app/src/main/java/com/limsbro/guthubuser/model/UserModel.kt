package com.limsbro.guthubuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatar_url: String,
    val gravatar_id: String,
    val html_url: String,
    val type: String,
    val notes: String? = null
)