package com.example.marksapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.marksapp.data.Event
import com.example.marksapp.data.EventsDao
import com.example.marksapp.data.EventsDatabase

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var eventsDao = EventsDatabase.getInstance(getApplication())?.eventsDao()

    fun getEvents(): LiveData<List<Event>>? {
        return eventsDao?.notes
    }
}