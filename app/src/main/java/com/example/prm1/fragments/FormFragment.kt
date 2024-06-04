package com.example.prm1.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.prm1.databinding.FragmentFormBinding
import com.example.prm1.model.FormType
import com.example.prm1.viewmodel.FormViewModel

private const val TYPE_KEY = "type"


class FormFragment : Fragment() {
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private val viewModel by viewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFormBinding.inflate(layoutInflater, container, false).also {
            binding = it
            binding.viewModel = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewModel) {
            init((type as? FormType.Edit)?.id)
            navigation.observe(viewLifecycleOwner) {
                it.resolve(findNavController())
            }
        }
    }


}