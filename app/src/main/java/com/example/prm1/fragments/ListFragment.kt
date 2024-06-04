package com.example.prm1.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm1.adapters.ProductListAdapter
import com.example.prm1.databinding.FragmentListBinding
import com.example.prm1.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var productListAdapter: ProductListAdapter

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
        setupSortButton()
        setupResetButton()

        productListAdapter = ProductListAdapter(
            onItemClick = { position -> viewModel.onProductEdit(productListAdapter.productList[position].id) },
            onItemLongClick = { position ->
                val selectedFood = productListAdapter.productList[position]
                AlertDialog.Builder(requireContext())
                    .setTitle("Usuń przedmiot")
                    .setMessage("Czy napewno chcesz usunąć ${selectedFood.name}?")
                    .setPositiveButton("Usuń") { dialog, _ ->
                        viewModel.onProductRemove(selectedFood.id)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Anuluj") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        )


        binding.sort.setOnClickListener {
            showCategoryDialog()
        }

        viewModel.products.observe(viewLifecycleOwner) {
            productListAdapter.productList = it
            binding.itemCount.text = Editable.Factory.getInstance().newEditable(it.size.toString())
        }


        binding.foodList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productListAdapter
        }

        binding.add.setOnClickListener {
            viewModel.onAddDish()
        }

        binding.sortState.setOnClickListener {
            showStateDialog()
        }

        viewModel.products.observe(viewLifecycleOwner) {
            productListAdapter.productList = it
        }

        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }
    }

    private fun showCategoryDialog() {
        val categories = arrayOf("Produkty Spożywcze", "Kosmetyki", "Leki") // Dostępne kategorie

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Wybierz kategorię")
            .setItems(categories) { _, which ->
                val selectedCategory = categories[which]
                filterProductsByCategory(selectedCategory)
            }
            .setNegativeButton("Anuluj") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showStateDialog() {
        val states = arrayOf("Ważny", "Przeterminowany")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Wybierz stan produktu")
            .setItems(states) { _, which ->
                val selectedState = states[which]
                filterProductsByState(selectedState)
            }
            .setNegativeButton("Anuluj") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun filterProductsByState(state: String) {
        val filteredProducts = when (state) {
            "Ważny" -> viewModel.products.value?.filter { !it.isExpired() }
            "Przeterminowany" -> viewModel.products.value?.filter { it.isExpired() }
            else -> viewModel.products.value
        }
        filteredProducts?.let {
            productListAdapter.updateList(it)
            binding.itemCount.text = Editable.Factory.getInstance().newEditable(it.size.toString())
        }
    }

    private fun filterProductsByCategory(category: String) {
        val filteredProducts = viewModel.products.value?.filter { it.category == category }
        filteredProducts?.let {
            productListAdapter.updateList(it)
            binding.itemCount.text = Editable.Factory.getInstance().newEditable(it.size.toString())
        }
    }

    private fun setupSortButton() {
        binding.sort.setOnClickListener {
            showCategoryDialog()
        }
    }

    private fun setupResetButton() {
        binding.reset.setOnClickListener {
            resetFilter()
        }
    }

    private fun resetFilter() {
        productListAdapter.updateList(viewModel.products.value ?: emptyList())
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
