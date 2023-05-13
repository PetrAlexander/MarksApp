package com.example.marksapp.presentation

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MainApplication : Application() {
    private val MAPKIT_API_KEY = "c6e96dca-7701-431c-8465-eeda635c1bd7"
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}
