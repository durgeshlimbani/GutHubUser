package com.limsbro.guthubuser.localdb

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.limsbro.guthubuser.model.UserModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Limsbro on 02,March,2022.
 */
@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {

    private lateinit var dataDao: DataDao
    private lateinit var db: UserDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, UserDatabase::class.java
        ).build()
        dataDao = db.dataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadLanguage() = runBlocking {

        val users: List<UserModel> = arrayListOf(
            UserModel(
                2,
                "defunkt",
                "https://avatars.githubusercontent.com/u/2?v=4",
                "test....",
                "https://github.com/defunkt",
                "User",
                null
            ),
            UserModel(
                1,
                "mojombo",
                "https://avatars.githubusercontent.com/u/1?v=4",
                "test....",
                "https://github.com/mojombo",
                "User",
                null
            ),
            UserModel(
                3,
                "pjhyett",
                "https://avatars.githubusercontent.com/u/3?v=4",
                "test....",
                "https://github.com/pjhyett",
                "User",
                null
            )
        )
        dataDao.insertAllUsers(users)
    }
}