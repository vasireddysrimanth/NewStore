package com.dev.storeapp.presentation.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.app.constants.Constants.CATEGORY
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.local.entity.asProductModel
import com.dev.storeapp.data.local.entity.toAddToCartEntity
import com.dev.storeapp.databinding.FragmentProductBinding
import com.dev.storeapp.presentation.adapter.ProductAdapter
import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
import com.dev.storeapp.presentation.ui.dialog.AddProductDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>() {
    private val viewModel by viewModels<ProductsViewModel>()
    private val cartsViewModel: AddToCartViewModel by viewModels()
    private lateinit var adapter: ProductAdapter
    private var allProducts = listOf<ProductEntity>()
    private var category: String? = null

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
        fun newInstance(category: String) = ProductFragment().apply {
            arguments = bundleOf(CATEGORY to category)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentProductBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = arguments?.getString(CATEGORY)
        adapter = ProductAdapter()
        initViews()
        createObserver()
    }

    private fun initViews() {
        adapter.setOnItemClickListener { product ->
            goToProductDetails(product.asProductModel())
        }
        adapter.setOnAddToCartClickListener { productEntity ->
            cartsViewModel.upsertToCart(productEntity.toAddToCartEntity())
        }

        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProducts(newText)
                return true
            }
        })

        binding.btnShowDialog.setOnClickListener {
            val showAddProductDialogFragment = AddProductDialogFragment.newInstance()
            showAddProductDialogFragment.show(childFragmentManager, AddProductDialogFragment.TAG)
        }
    }

    private fun createObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it.status) {
                        DataStatus.Status.LOADING -> showLoader()
                        DataStatus.Status.SUCCESS -> {
                            hideLoader()
                            allProducts = it.data ?: emptyList()
                            filterProducts()
                        }
                        DataStatus.Status.ERROR -> showError()
                        DataStatus.Status.EMPTY -> {}
                    }
                }
            }
        }
    }

    private fun filterProducts(searchText: String? = null) {
        var filteredList = allProducts

        if (!category.isNullOrEmpty()) {
            filteredList = allProducts.filter { product ->
                product.category?.contains(category!!, ignoreCase = true) == true
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "No products found for '$category'. Showing all products.",
                    Toast.LENGTH_SHORT
                ).show()
                filteredList = allProducts
            }
        }

        if (!searchText.isNullOrEmpty()) {
            filteredList = filteredList.filter { product ->
                product.title?.contains(searchText, ignoreCase = true) == true
            }
        }

        adapter.differ.submitList(filteredList)
        binding.productRecyclerView.adapter = adapter
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
    }

    private fun goToProductDetails(product: com.dev.storeapp.data.model.Product) {
        replaceFragment(R.id.fragment_container, ProductDetailsFragment.newInstance(product.id), true)
    }
}