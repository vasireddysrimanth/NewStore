package com.dev.storeapp.presentation.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.constants.Constants.KEY_PRODUCT_ID
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.local.entity.asProductModel
import com.dev.storeapp.data.local.entity.toAddToCartEntity
import com.dev.storeapp.databinding.FragmentProductDetailsBinding
import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
import com.dev.storeapp.presentation.ui.checkout.CheckOutFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>(), View.OnClickListener {

    private val viewModel: AddToCartViewModel by viewModels()
    private val productViewModel: ProductsViewModel by viewModels()
    private var product: ProductEntity? = null

    companion object {
        fun newInstance(productId: Int) = ProductDetailsFragment().apply {
            arguments = Bundle().apply { putInt(KEY_PRODUCT_ID, productId) }
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) =
        FragmentProductDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductData()
        setupViews()
        observeViewModels()
    }

    private fun setupProductData() {
        arguments?.getInt(KEY_PRODUCT_ID)?.takeIf { it != -1 }?.let {
            productViewModel.getProductById(it)
        }
    }

    private fun setupViews() = binding.apply {
        productDetailToolbar.setNavigationOnClickListener { goBack() }
        buyNowButton.setOnClickListener(this@ProductDetailsFragment)
        addToCart.setOnClickListener(this@ProductDetailsFragment)
        removeFromCart.setOnClickListener(this@ProductDetailsFragment)
    }

    private fun observeViewModels() {
        launchAndRepeatOnStarted {
            viewModel.isInCart.collect { updateCartButtonsVisibility(it) }
        }
        launchAndRepeatOnStarted {
            productViewModel.productIdUiState.collect { handleProductResult(it) }
        }
    }

    private fun handleProductResult(result: Result<ProductEntity>) {
        when (result) {
            is Result.Loading -> showLoader()
            is Result.Success -> {
                hideLoader()
                product = result.data
                updateUi(result.data)
                viewModel.isProductInCart(result.data.id)
            }
            is Result.Error -> {
                hideLoader()
                Toast.makeText(requireContext(), "Error loading product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCartButtonsVisibility(isInCart: Boolean) = binding.apply {
        addToCart.visibility = if (isInCart) View.GONE else View.VISIBLE
        removeFromCart.visibility = if (isInCart) View.VISIBLE else View.GONE
    }

    private fun updateUi(product: ProductEntity) = binding.apply {
        productTitle.text = product.title
        productBrand.text = product.brand
        productColor.text = product.color
        productCategory.text = product.category
        productDiscount.text = "${product.discount}%"
        productModel.text = product.model
        productPrice.text = "$${product.price}"
        productDescription.text = product.description
        Glide.with(requireContext())
            .load(product.image)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(productImage)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buyNowButton -> product?.let {
                replaceFragment(R.id.fragment_container, CheckOutFragment.newInstance(it.asProductModel()), true)
            } ?: showToast("Product not loaded")
            R.id.addToCart -> product?.let {
                viewModel.upsertToCart(it.toAddToCartEntity())
                showToast("Item Added to Cart!")
            } ?: showToast("Product not loaded")
            R.id.removeFromCart -> product?.let {
                viewModel.deleteCartById(it.id)
                showToast("Item Removed from Cart!")
            } ?: showToast("Product not loaded")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}