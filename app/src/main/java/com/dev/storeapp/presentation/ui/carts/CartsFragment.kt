package com.dev.storeapp.presentation.ui.carts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.asProductModel
import com.dev.storeapp.databinding.FragmentCartsBinding
import com.dev.storeapp.presentation.adapter.CartsAdapter
import com.dev.storeapp.presentation.ui.checkout.CheckOutFragment
import com.dev.storeapp.presentation.ui.products.ProductDetailsFragment
import com.dev.storeapp.presentation.ui.products.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartsFragment : BaseFragment<FragmentCartsBinding>(),View.OnClickListener {

    private val viewModel by viewModels<AddToCartViewModel>()
    private lateinit var adapter :CartsAdapter
    companion object {
        const val TAG = "CartsFragment"
        fun newInstance() = CartsFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentCartsBinding {
        return FragmentCartsBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartsAdapter()
        initViews()
        createObserver()
    }

    private fun initViews() {
        binding.clearCart.setOnClickListener(this)
        binding.buyAll.setOnClickListener(this)

        binding.apply {
            recyclerView.adapter = adapter
            buttonBack.setOnClickListener {
                goBack()
                goBack()
            }
            adapter.setOnItemClickListener { product ->
                replaceFragment(R.id.fragment_container,ProductDetailsFragment.newInstance(product.asProductModel()),true)
            }
        }
    }

    private fun deleteAllCarts() {
        viewModel.deleteAllCarts()
    }

    private fun createObserver() {
        launchAndRepeatOnStarted {
            viewModel.uiState.collect {
                when(it){
                    is Result.Loading  ->{
                        showLoader()
                        AppLogger.d("CartsFragment","Loading...!")
                    }
                    is Result.Success -> {
                        hideLoader()
                        adapter.differ.submitList(it.data)
                        updateCartButtonsVisibility(it.data.isEmpty())
                    }
                    is Result.Error -> {
                        AppLogger.e("CartsFragment","Error: ${it.exception}")
                    }
                }
            }
        }
    }

    private fun updateCartButtonsVisibility(isCartEmpty: Boolean) {
        binding.clearCart.visibility = if (isCartEmpty) View.INVISIBLE else View.VISIBLE
        binding.buyAll.visibility = if (isCartEmpty) View.INVISIBLE else View.VISIBLE
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.clearCart ->{
                deleteAllCarts()
                AppLogger.d("CartsFragment","All carts deleted")
            }
            binding.buyAll -> {
                val cartItems = adapter.differ.currentList
                val allCartItems = cartItems.map { it.asProductModel() }
                replaceFragment(R.id.fragment_container, CheckOutFragment.newInstance(allCartItems), true)
                AppLogger.d("CartsFragment","Buy all clicked")
            }
            else -> {
                AppLogger.d("CartsFragment","Unknown click action")
            }

        }

    }


}