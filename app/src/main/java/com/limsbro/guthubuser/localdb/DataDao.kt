package com.limsbro.guthubuser.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.limsbro.guthubuser.model.UserModel

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
@Dao
interface DataDao {

    //here insert all loaded user list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(userData: List<UserModel>)

    //get selected user note
    @Query("SELECT * from users WHERE id = :userId")
    fun getUserNote(userId: Int): LiveData<UserModel>

    //Updating only notes in user table By id
    @Query("UPDATE users SET notes=:notes WHERE id = :userId")
    fun updateNotes(notes : String, userId: Int)

    //get all users list (with search text or all)
    @Query("SELECT * from users WHERE login like :search OR notes LIKE :search ORDER BY id ASC")
    fun getAllSavedUserData(search: String?): LiveData<List<UserModel>>
}