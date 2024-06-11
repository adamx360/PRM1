package com.example.prm1.data

import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity
import java.time.LocalDate

object NoteRepositoryInMemory : NoteRepository {
    private val noteLists = mutableListOf(
        Note(1, "Milk", "sadasd", LocalDate.now().toString(), "/external_primary/images/media/51", null, "Warszawa")
    )

    override suspend fun getNoteList(): List<Note> {
        noteLists.sortBy { it.date }
        return noteLists.toList()
    }

    override suspend fun add(note: Note) {

        noteLists.add(if (note.id == 0) note.copy(id = getNextId()) else note)
        noteLists.sortBy { it.date }
    }

    override suspend fun getNoteById(id: Int): Note {
        return noteLists.find { it.id == id }
            ?: throw NoSuchElementException("Note with id $id not found")
    }

    override suspend fun set(note: Note) {
        val index = noteLists.indexOfFirst { it.id == note.id }
        if (index != -1 && isValidNote(note)) {
            noteLists[index] = note
            noteLists.sortBy { it.date }
        }
    }

    override suspend fun removeById(id: Int) {
        noteLists.removeIf { it.id == id }
        noteLists.sortBy { it.date }
    }

    private fun getNextId(): Int {
        return noteLists.maxOfOrNull { it.id }?.inc() ?: 1
    }

    private fun isValidNote(note: Note): Boolean {
        return note.name.isNotBlank()
    }
}
