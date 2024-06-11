package com.example.prm1.data

import android.content.Context
import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity.Companion.toEntity
import com.example.prm_proj2.db.NoteDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepositoryInFile(val context: Context) : NoteRepository{
    val db: NoteDb = NoteDb.open(context)
    override suspend fun getNoteList(): List<Note> = withContext(Dispatchers.IO) {
        db.note.getAll().map { it.toNote(context) }
    }


    override suspend fun add(note: Note) = withContext(Dispatchers.IO) {
        db.note.createOrUpdate(note.toEntity())
    }

    override suspend fun getNoteById(id: Int): Note = withContext(Dispatchers.IO){
        db.note.getById(id.toLong()).toNote(context)
    }

    override suspend fun set(note: Note) = withContext(Dispatchers.IO){
        db.note.createOrUpdate(note.toEntity())
    }

    override suspend fun removeById(id: Int) = withContext(Dispatchers.IO){
        db.note.remove(id.toLong())
    }
}