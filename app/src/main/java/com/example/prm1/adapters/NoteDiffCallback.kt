package com.example.prm1.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.prm1.model.Note
import com.example.prm1.model.db.NoteEntity

class NoteDiffCallback(
    private val old: List<Note>,
    private val new: List<Note>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] === new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]
}