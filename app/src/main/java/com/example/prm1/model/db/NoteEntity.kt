package com.example.prm1.model.db


import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.prm1.model.Note

@Entity(tableName = "note_notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val note: String,
    val date: String,
    val picturePath: String?,
    val voiceRecordingPath: String?,
    val city: String,
) {
    fun toNote(context: Context): Note {
        return Note(id, name, note, date, picturePath, voiceRecordingPath, city)
    }

    companion object {
        fun Note.toEntity(): NoteEntity {
            return NoteEntity(id, name, note, date, picturePath, voiceRecordingPath, city)
        }
    }
}
