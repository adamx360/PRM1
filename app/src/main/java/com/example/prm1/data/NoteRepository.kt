package com.example.prm1.data

import com.example.prm1.model.Note

interface NoteRepository {

    suspend fun getNoteList(): List<Note>
    suspend fun add(note: Note)
    suspend fun getNoteById(id: Int): Note
    suspend fun set(note: Note)
    suspend fun removeById(id: Int)

    companion object {
        const val GENERATE_ID = 0
    }
}