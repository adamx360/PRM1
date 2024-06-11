package com.example.prm1.model


import androidx.room.Entity
import androidx.room.PrimaryKey

data class Note(
    val id: Int,
    val name: String,
    val note: String,
    val date: String,
    val picturePath: String?,
    val voiceRecordingPath: String?,
    val city: String,
)
