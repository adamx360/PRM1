package com.example.prm1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prm1.R
import com.example.prm1.data.ProductRepository
import com.example.prm1.data.RepositoryLocator
import com.example.prm1.model.Product
import com.example.prm1.model.navigation.Destination
import com.example.prm1.model.navigation.PopBack
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormViewModel : ViewModel() {
    private val repository: ProductRepository = RepositoryLocator.productRepository
    private var edited: Product? = null

    val name = MutableLiveData("")
    val count = MutableLiveData<String>()
    val selectedYear = MutableLiveData<Int>()
    val selectedMonth = MutableLiveData<Int>()
    val selectedDay = MutableLiveData<Int>()
    private val selectedCategory = MutableLiveData<String>()
    val buttonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()

    private fun setDate(year: Int, month: Int, day: Int) {
        selectedYear.value = year
        selectedMonth.value = month
        selectedDay.value = day
    }

    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    fun init(id: Int?) {
        edited = id?.let {
            repository.getProductById(id)
        }?.also {
            name.value = it.name
            count.value = it.count ?: ""
            it.date.let { date ->
                val calendar = parseDate(date)
                setDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
            setSelectedCategory(it.category)
        }

        buttonText.value = when (edited) {
            null -> R.string.add
            else -> R.string.save
        }
    }


    fun onSave() {
        val name = name.value.orEmpty()
        val count = count.value.orEmpty()
        val year = selectedYear.value ?: 0
        val month = selectedMonth.value ?: 0
        val day = selectedDay.value ?: 0
        val category = selectedCategory.value.orEmpty()

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val formattedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val toSave = edited?.copy(
            name = name,
            count = count.toIntOrNull().toString(),
            date = formattedDate,
            category = category
        ) ?: Product(
            id = repository.getNextId(),
            name = name,
            count = count.toIntOrNull().toString(),
            date = formattedDate,
            category = category
        )

        if (edited == null) {
            repository.add(toSave)
        } else {
            repository.set(toSave.id, toSave)
        }
        navigation.value = PopBack()
    }

    private fun parseDate(dateString: String): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Calendar.getInstance()
        date.time = dateFormat.parse(dateString) ?: Date()
        return date
    }
}
