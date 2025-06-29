package com.dev.storeapp.presentation.ui.carts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.data.local.entity.asProductModel
import com.dev.storeapp.databinding.FragmentCartsBinding
import com.dev.storeapp.presentation.adapter.CartsAdapter
import com.dev.storeapp.presentation.ui.checkout.CheckOutFragment
import com.dev.storeapp.presentation.ui.products.ProductDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartsFragment : BaseFragment<FragmentCartsBinding>(), View.OnClickListener {

    private val viewModel by viewModels<AddToCartViewModel>()
    private lateinit var adapter: CartsAdapter

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
                replaceFragment(R.id.fragment_container, ProductDetailsFragment.newInstance(product.id), true)
            }

            adapter.setOnDeleteItemClickListener { product ->
                deleteCartItem(product)
            }

            adapter.setOnQuantityChangeListener { product, newQuantity ->
                updateCartItemQuantity(product, newQuantity)
            }
        }
    }

    private fun deleteAllCarts() {
        viewModel.deleteAllCarts()
    }

    private fun deleteCartItem(product: AddToCartEntity) {
        viewModel.deleteCartById(product.id)
        Toast.makeText(requireContext(),"item removed  fromo Cart ..!",Toast.LENGTH_SHORT).show()
        AppLogger.d("CartsFragment", "Item deleted: ${product.title}")
    }

    private fun updateCartItemQuantity(product: AddToCartEntity, newQuantity: Int) {
        if (newQuantity <= 0) {
            deleteCartItem(product)
        } else {
            val updatedProduct = product.copy(quantity = newQuantity)
            viewModel.upsertToCart(updatedProduct)
            Toast.makeText(requireContext(),"item added To Cart ..!",Toast.LENGTH_SHORT).show()
            AppLogger.d("CartsFragment", "Quantity updated: ${product.title} -> $newQuantity")
        }
    }

    private fun createObserver() {
        launchAndRepeatOnStarted {
            viewModel.uiState.collect {
                when (it) {
                    is Result.Loading -> {
                        showLoader()
                        AppLogger.d("CartsFragment", "Loading...!")
                    }
                    is Result.Success -> {
                        hideLoader()
                        val cartItems = it.data.filter { item -> (item.quantity ?: 1) > 0 }
                        adapter.differ.submitList(cartItems)
                        updateCartButtonsVisibility(cartItems.isEmpty())
                        updateTotalPrice(cartItems)
                    }
                    is Result.Error -> {
                        AppLogger.e("CartsFragment", "Error: ${it.exception}")
                    }
                }
            }
        }
    }

    private fun updateCartButtonsVisibility(isCartEmpty: Boolean) {
        binding.clearCart.visibility = if (isCartEmpty) View.INVISIBLE else View.VISIBLE
        binding.buyAll.visibility = if (isCartEmpty) View.INVISIBLE else View.VISIBLE
    }

    private fun updateTotalPrice(cartItems: List<AddToCartEntity>) {
        val totalPrice = cartItems.sumOf { item ->
            val quantity = item.quantity ?: 1
            val price = item.price ?: 0.0
            quantity * price
        }

        // Update total price in UI if you have a total price TextView
        // binding.txtTotalPrice.text = "Total: ₹${"%.2f".format(totalPrice)}"

        AppLogger.d("CartsFragment", "Total price: $totalPrice")
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.clearCart -> {
                deleteAllCarts()
                AppLogger.d("CartsFragment", "All carts deleted")
            }
            binding.buyAll -> {
                val cartItems = adapter.differ.currentList
                val allCartItems = cartItems.map { it.asProductModel() }
                replaceFragment(
                    R.id.fragment_container,
                    CheckOutFragment.newInstance(allCartItems),
                    true
                )
                AppLogger.d("CartsFragment", "Buy all clicked")
            }
            else -> {
                AppLogger.d("CartsFragment", "Unknown click action")
            }
        }
    }
}