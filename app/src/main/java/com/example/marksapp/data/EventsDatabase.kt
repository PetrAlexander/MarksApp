package com.example.marksapp.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventsDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao?

    companion object {
        private var instance: EventsDatabase? = null
        private const val DB_NAME = "events.db"
        fun getInstance(application: Application?): EventsDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    application!!,
                    EventsDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return instance
        }
    }
}