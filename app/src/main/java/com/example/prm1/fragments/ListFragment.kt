package com.example.prm1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm1.adapters.NoteListAdapter
import com.example.prm1.databinding.FragmentListBinding
import com.example.prm1.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var noteListAdapter: NoteListAdapter

    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it
                binding.viewModel = viewModel
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteListAdapter = NoteListAdapter(
            onItemClick = { position -> viewModel.updateNote(noteListAdapter.noteList[position]) },
            onItemLongClick = { position ->
                val selectedFood = noteListAdapter.noteList[position]
                AlertDialog.Builder(requireContext())
                    .setTitle("Usuń przedmiot")
                    .setMessage("Czy napewno chcesz usunąć ${selectedFood.name}?")
                    .setPositiveButton("Usuń") { dialog, _ ->
                        viewModel.onNoteRemove(selectedFood.id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Anuluj") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        )


        viewModel.notes.observe(viewLifecycleOwner) {
            noteListAdapter.noteList = it
        }


        binding.foodList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteListAdapter
        }

        binding.add.setOnClickListener {
            viewModel.onAddNote()
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            noteListAdapter.noteList = it
        }

        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(viewModel::onDestinationChange)
    }

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(viewModel::onDestinationChange)
        super.onStop()
    }
}
