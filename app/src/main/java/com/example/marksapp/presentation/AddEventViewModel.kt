package com.example.marksapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marksapp.data.Event
import com.example.marksapp.data.EventsDao
import com.example.marksapp.data.EventsDatabase

class AddEventViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var eventsDao: EventsDao? = null

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    suspend fun saveEvent(event: Event) {
        eventsDao = EventsDatabase.getInstance(getApplication())?.eventsDao()
        eventsDao?.add(event)
        finishWork()
    }

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}
