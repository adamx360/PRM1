package com.example.prm1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm1.R
import com.example.prm1.data.NoteRepository
import com.example.prm1.data.NoteRepository.Companion.GENERATE_ID
import com.example.prm1.data.RepositoryLocator
import com.example.prm1.fragments.LOC_CITY
import com.example.prm1.fragments.picPath
import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity
import com.example.prm1.model.navigation.AddPicture
import com.example.prm1.model.navigation.Destination
import com.example.prm1.model.navigation.PopBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormViewModel : ViewModel() {
    private val repository: NoteRepository = RepositoryLocator.noteRepository
    private var edited: Note? = null

    val name = MutableLiveData("")
    val buttonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()
    val note = MutableLiveData("")
    private val picturePath = MutableLiveData("")
    private val audioFilePath = MutableLiveData<String?>()
    private val city = MutableLiveData<String>()

    fun init(id: Int?) {
        id?.let {
            viewModelScope.launch {
                edited = repository.getNoteById(it)?.also {
                    withContext(Dispatchers.Main) {
                        name.value = it.name
                        parseDate(it.date)
                        note.value = it.note
                        picturePath.value = it.picturePath
                        audioFilePath.value = it.voiceRecordingPath
                        city.value = it.city
                    }
                }
            }
        }

        buttonText.value = when (edited) {
            null -> R.string.add
            else -> R.string.save
        }

    }

    fun onAddPicture() {
        navigation.value = AddPicture()
    }

    fun onSave() {
        val name = name.value.orEmpty()
        val note = note.value.orEmpty()
        val city = LOC_CITY
        val picturePath = picPath
        val formattedDate = LocalDate.now().toString()
        val toSave = edited?.copy(
            name = name,
            note = note,
            date = formattedDate,
            picturePath = picturePath,
            voiceRecordingPath = audioFilePath.value,
            city = city
        ) ?: Note(
            id = GENERATE_ID,
            name = name,
            note = note,
            date = formattedDate,
            picturePath = picturePath,
            voiceRecordingPath = audioFilePath.value,
            city = city
        )

        viewModelScope.launch {
            if (edited == null) {
                repository.add(toSave)
            } else {
                repository.set(toSave)
            }
            withContext(Dispatchers.Main) {
                navigation.value = PopBack()
            }
        }
    }

    private fun parseDate(dateString: String): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Calendar.getInstance()
        date.time = dateFormat.parse(dateString) ?: Date()
        return date
    }
}
