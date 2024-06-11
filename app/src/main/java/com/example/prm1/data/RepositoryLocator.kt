package com.example.prm1.data

import android.content.Context

object RepositoryLocator {
    lateinit var noteRepository: NoteRepository
    private set

    fun init(context: Context) {
        noteRepository = NoteRepositoryInFile(context)
    }
}
