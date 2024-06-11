package com.example.prm1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prm1.databinding.ItemNoteBinding
import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity

class NoteItem(private val itemViewBinding: ItemNoteBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {
    var id: Int = 0
        private set

    fun onBind(noteItem: Note, onItemClick: () -> Unit, onItemLongClick: () -> Unit) {
        with(itemViewBinding) {
            id = noteItem.id
            title.text = noteItem.name
            subtitle.text = noteItem.date
            city.text = noteItem.city
            root.setOnClickListener {
                onItemClick()
            }
            root.setOnLongClickListener {
                onItemLongClick()
                return@setOnLongClickListener true
            }
        }
    }
}

class NoteListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<NoteItem>() {
    var noteList: List<Note> = emptyList()
        set(value) {
            val diffs = DiffUtil.calculateDiff(NoteDiffCallback(field, value))
            field = value
            diffs.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(layoutInflater, parent, false)
        return NoteItem(binding)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteItem, position: Int) {
        holder.onBind(
            noteList[position],
            onItemClick = { onItemClick(position) },
            onItemLongClick = { onItemLongClick(position) })
    }
}