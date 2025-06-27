package com.dev.storeapp.presentation.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.local.entity.asProductModel
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.databinding.FragmentProductBinding
import com.dev.storeapp.presentation.adapter.ProductAdapter
import com.dev.storeapp.presentation.ui.dialog.AddProductDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : BaseFragment<FragmentProductBinding>() {
    private val viewModel by viewModels<ProductsViewModel>()
    private lateinit var adapter :ProductAdapter
    //create a list for product List
    private var productList  = listOf<ProductEntity>()



    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentProductBinding.inflate(layoutInflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductAdapter()
        initViews()
        createObserver()
    }

    private fun initViews() {
        adapter.setOnItemClickListener {product->
            goToProductDetails(product.asProductModel())
        }

        binding.txtSearch.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = if (newText.isNullOrEmpty()) {
                    productList
                } else {
                    productList.filter { product ->
                        product.title!!.contains(newText, ignoreCase = true)
                    }
            }
                adapter.differ.submitList(filteredList)
                return true
            }
        })

        binding.btnShowDialog.setOnClickListener{
            val showAddProductDialogFragment = AddProductDialogFragment.newInstance()
            showAddProductDialogFragment.show(childFragmentManager, AddProductDialogFragment.TAG)
        }
    }


    private fun createObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect{
                        when (it.status) {
                            DataStatus.Status.LOADING -> { showLoader() }
                            DataStatus.Status.SUCCESS -> it.data?.let { users->
                                hideLoader()
                                productList  = users
                                adapter.differ.submitList(productList)
                                binding.productRecyclerView.adapter = adapter
                            }
                            DataStatus.Status.ERROR -> showError()
                            DataStatus.Status.EMPTY -> {}
                        }

                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Something went Wrong..!", Toast.LENGTH_SHORT).show()
    }

    private fun goToProductDetails(product:Product){
        replaceFragment(R.id.fragment_container,ProductDetailsFragment.newInstance(product.id),true)
    }
}