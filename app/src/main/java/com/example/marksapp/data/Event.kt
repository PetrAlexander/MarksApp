package com.example.marksapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String
) {
    constructor(name: String, address: String) : this(0, name, address)
}