/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val nights = listOf(
                SleepNight(1L, System.currentTimeMillis(), System.currentTimeMillis() + 10000L, 1),
                SleepNight(2L, System.currentTimeMillis() + 10L, System.currentTimeMillis() + 20000L, 2))
        nights.forEach { night ->
            sleepDao.insert(night)
        }
//        val night = SleepNight()
//        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(2, tonight?.sleepQuality)
    }

    @Test
    @Throws(Exception::class)
    fun insertListAndGetById() {
        val nights = listOf(
                SleepNight(1L, System.currentTimeMillis(), System.currentTimeMillis() + 10000L, 1),
                SleepNight(2L, System.currentTimeMillis() + 10L, System.currentTimeMillis() + 20000L, 2))
        nights.forEach { night ->
            sleepDao.insert(night)
        }
        val byId = sleepDao.get(2L)
        assertEquals(2, byId?.sleepQuality)
    }

    @Test
    @Throws(Exception::class)
    fun insertListAndGetAllNights() {
        val nights = listOf(
                SleepNight(1L, System.currentTimeMillis(), System.currentTimeMillis() + 10000L, 1),
                SleepNight(2L, System.currentTimeMillis() + 10L, System.currentTimeMillis() + 20000L, 2))
        nights.forEach { night ->
            sleepDao.insert(night)
        }
        val allNights = sleepDao.getAllNights()
        val nightsList = allNights.value
        assertNotNull(nightsList)
    }
}

