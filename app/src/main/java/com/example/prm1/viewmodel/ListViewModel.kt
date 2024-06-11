package com.example.prm1.viewmodel

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.prm1.R
import com.example.prm1.data.NoteRepository
import com.example.prm1.data.RepositoryLocator
import com.example.prm1.model.Note
import com.example.prm1.model.navigation.AddNote
import com.example.prm1.model.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val repository: NoteRepository = RepositoryLocator.noteRepository
    val navigation = MutableLiveData<Destination>()
    val notes: MutableLiveData<List<Note>> = MutableLiveData(emptyList())

    init {
        this.loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch(Dispatchers.Main) {
            notes.value = repository.getNoteList()
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.add(note)
            loadNotes()
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.set(note)
            loadNotes()
        }
    }

    fun onNoteRemove(id: Int) {
        viewModelScope.launch {
            repository.removeById(id)
            loadNotes()
        }
    }

    fun handleImageUri(contentResolver: ContentResolver, uri: Uri) {
        val takeFlags: Int =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }

    fun onDestinationChange(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == R.id.listFragment) {
            this.loadNotes()
        }
    }

    fun onAddNote() {
        navigation.value = AddNote()
    }

}
