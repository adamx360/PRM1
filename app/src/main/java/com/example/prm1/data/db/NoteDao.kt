package com.example.prm1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prm1.model.db.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_notes;")
    suspend fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM note_notes WHERE id = :id;")
    suspend fun getById(id: Long): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(note: NoteEntity)

    @Query("DELETE FROM note_notes WHERE id = :noteId")
    suspend fun remove(noteId: Long)
}