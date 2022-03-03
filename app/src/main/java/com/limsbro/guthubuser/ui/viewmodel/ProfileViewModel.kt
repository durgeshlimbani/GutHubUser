package com.limsbro.guthubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.limsbro.guthubuser.http.RetrofitHelper
import com.limsbro.guthubuser.localdb.DataDao
import com.limsbro.guthubuser.localdb.UserDatabase
import com.limsbro.guthubuser.model.UserProfileModel
import com.limsbro.guthubuser.ui.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val mainRepository: MainRepository
    private val dataDao: DataDao

    val userProfileModel = MutableLiveData<UserProfileModel>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    init {
        val retrofitService = RetrofitHelper.getInstance()
        dataDao = UserDatabase.getInstance(app).dataDao()
        mainRepository = MainRepository(dataDao, retrofitService)
    }

    fun getUserProfileFromAPI(username: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.loadUserProfileAPI(username)
            response.enqueue(object : Callback<UserProfileModel> {
                override fun onResponse(
                    call: Call<UserProfileModel>,
                    response: Response<UserProfileModel>
                ) {
                    loading.value = false
                    if (response.isSuccessful) {
                        userProfileModel.postValue(response.body())
                    } else errorMessage.value = "Error while loading user profile"
                }

                override fun onFailure(call: Call<UserProfileModel>, t: Throwable) {
                    errorMessage.value = "Error while loading user profile"
                    loading.value = false
                }
            })
        }

    }

    fun saveNote(userID: Int, notes: String) {
        UserDatabase.databaseWriteExecutor.execute {
            dataDao.updateNotes(notes, userID)
        }
        errorMessage.value = "success"
    }

    fun getNotes(userId: Int) = mainRepository.getSavedNoteData(userId)
}