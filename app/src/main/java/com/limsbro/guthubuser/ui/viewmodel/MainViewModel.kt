package com.limsbro.guthubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.limsbro.guthubuser.http.RetrofitHelper
import com.limsbro.guthubuser.localdb.DataDao
import com.limsbro.guthubuser.localdb.UserDatabase
import com.limsbro.guthubuser.model.UserModel
import com.limsbro.guthubuser.ui.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Limsbro on 01,March,2022.
 */
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val mainRepository: MainRepository
    private val dataDao: DataDao

    private val searchMutableLive = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    val allSavedUserData: LiveData<List<UserModel>>
        get() = Transformations.switchMap(searchMutableLive) { input: String ->
            mainRepository.getAllSavedPlanData(
                "%$input%"
            )
        }

    fun setSearchFilterData(searchValue: String) {
        searchMutableLive.value = searchValue
    }

    fun getUserDataFromAPI(lastLoad: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = mainRepository.loadDataFromAPI(lastLoad)
            response.enqueue(object : Callback<List<UserModel>> {
                override fun onResponse(
                    call: Call<List<UserModel>>,
                    response: Response<List<UserModel>>
                ) {
                    loading.value = false
                    if (response.body() != null)
                        UserDatabase.databaseWriteExecutor.execute {
                            dataDao.insertAllUsers(response.body() as List<UserModel>)
                        }
                    else errorMessage.value = "Error while loading new data, may be your daily limit over without authorization."
                }

                override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                    loading.value = false
                    errorMessage.value = "Error while loading new data"
                }
            })
        }

    }

    init {
        val retrofitService = RetrofitHelper.getInstance()
        dataDao = UserDatabase.getInstance(app).dataDao()
        mainRepository = MainRepository(dataDao, retrofitService)
    }

}