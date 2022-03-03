package com.limsbro.guthubuser.ui.repository

import androidx.lifecycle.LiveData
import com.limsbro.guthubuser.http.RetrofitHelper
import com.limsbro.guthubuser.localdb.DataDao
import com.limsbro.guthubuser.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
class MainRepository(private val dataDao: DataDao, private val retrofitService: RetrofitHelper) {

    fun getAllSavedPlanData(searchStr: String?): LiveData<List<UserModel>> {
        return dataDao.getAllSavedUserData(searchStr)
    }

    fun getSavedNoteData(userID: Int): LiveData<UserModel> {
        return dataDao.getUserNote(userID)
    }

    fun loadDataFromAPI(lastLoad: String) = retrofitService.getUserList(lastLoad)

    fun loadUserProfileAPI(username: String) = retrofitService.getUserProfile(username)
}
