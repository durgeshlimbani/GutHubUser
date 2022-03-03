package com.limsbro.guthubuser.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.limsbro.guthubuser.model.UserModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Durgesh Patel on 01,March,2022.
 */
@Database(entities = [UserModel::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun dataDao() : DataDao

    companion object {

        @Volatile
        private var databaseInstance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase =
            databaseInstance ?: synchronized(this) {
                databaseInstance ?: Room.databaseBuilder(context.applicationContext,
                    UserDatabase::class.java, "UserDb")
                    .build().also { databaseInstance = it }
            }

        private const val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    }
}