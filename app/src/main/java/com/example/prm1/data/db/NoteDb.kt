// database/DiaryDatabase.kt
package com.example.prm_proj2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.prm1.data.db.NoteDao
import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalTime

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDb: RoomDatabase() {
    abstract val note: NoteDao
    companion object{
        fun open(context: Context): NoteDb{
            return Room.databaseBuilder(
                context, NoteDb::class.java, "note_notes"
            ).build()
        }
    }

}
