package com.example.marksapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventsDao {

    @get:Query("Select * from events")
    val notes: LiveData<List<Event>>

    @Insert
    suspend fun add(event: Event?)
}



