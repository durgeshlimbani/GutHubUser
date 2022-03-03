package com.limsbro.guthubuser.helper

import com.limsbro.guthubuser.model.UserModel

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
interface UserItemClickListener {
    fun onItemClickListener(userModel: UserModel)
}