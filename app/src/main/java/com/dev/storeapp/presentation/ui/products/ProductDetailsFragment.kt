package com.dev.storeapp.presentation.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.data.model.toAddToCartEntity
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
        const val TAG = "ProductDetailsFragment"
        const val KEY_PRODUCT = "product"

        fun newInstance() = ProductDetailsFragment()

        fun newInstance(productId: Int) = ProductDetailsFragment().apply {
            arguments = bundleOf(KEY_PRODUCT_ID to productId)
        }

        fun newInstance(product: Product): ProductDetailsFragment {
            return ProductDetailsFragment().apply {
                arguments = bundleOf(KEY_PRODUCT to product)
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentProductDetailsBinding {
        return FragmentProductDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductData()
        initViews()
        createObserver()
    }

    private fun setupProductData() {
        arguments?.let { args ->
            // Try to get product directly first
            product = args.getParcelable(KEY_PRODUCT)

            // If no product passed directly, try to get by ID
            if (product == null) {
                val productId = args.getInt(KEY_PRODUCT_ID, -1)
                if (productId != -1) {
                    // Fetch product by ID using productViewModel
                    productViewModel.getProductById(productId)
                }
            } else {
                // If we have the product, update UI immediately
            }
        }
    }

    private fun initViews() {
        binding.productDetailToolbar.setOnClickListener(this)
        binding.buyNowButton.setOnClickListener(this)
        binding.addToCart.setOnClickListener(this)
        binding.removeFromCart.setOnClickListener(this)

        // Set toolbar title if we have product
        product?.let {
            binding.toolbarTitle.text = it.title
            viewModel.isProductInCart(it.id)
        }
    }

    private fun createObserver() {
        launchAndRepeatOnStarted {
            // Observe cart status
            viewModel.isInCart.collect { isInCart ->
                updateCartButtonsVisibility(isInCart)
            }
        }

        launchAndRepeatOnStarted {
            // Observe product data (when fetched by ID)
            productViewModel.productIdUiState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        // Show loading indicator if needed
                        showLoader()
                        Log.d(TAG, "Loading product...")
                    }
                    is Result.Success -> {
                        hideLoader()
                        product = result.data
                        updateUi(result.data)
                        // Check cart status for the loaded product
                        result.data?.let {
                            binding.toolbarTitle.text = it.title
                            viewModel.isProductInCart(it.id)
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error loading product: ${result.exception?.message}")
                        Toast.makeText(requireContext(), "Error loading product", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateCartButtonsVisibility(isInCart: Boolean) {
        if (isInCart) {
            binding.addToCart.visibility = View.GONE
            binding.removeFromCart.visibility = View.VISIBLE
        } else {
            binding.addToCart.visibility = View.VISIBLE
            binding.removeFromCart.visibility = View.GONE
        }
    }

    private fun updateUi(product: ProductEntity?) {
        product?.let { prod ->
            binding.apply {
                titleText.text = prod.title
                brandText.text = prod.brand
                colorText.text = prod.color
                categoryText.text = prod.category
                discountText.text = "${prod.discount}%"
                modelText.text = prod.model
                priceText.text = "$${prod.price}"
                descriptionText.text = prod.description

                Glide.with(requireContext())
                    .load(prod.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(productImage)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.productDetailToolbar -> {
                goBack()
            }
            binding.buyNowButton -> {
                product?.let { prod ->
                    replaceFragment(R.id.fragment_container, CheckOutFragment.newInstance(prod.asProductModel()), true)
                } ?: run {
                    Toast.makeText(requireContext(), "Product not loaded", Toast.LENGTH_SHORT).show()
                }
            }
            binding.addToCart -> {
                product?.let { prod ->
                    viewModel.insertToCart(prod.toAddToCartEntity())
                    Toast.makeText(requireContext(), "Item Added to Cart!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(requireContext(), "Product not loaded", Toast.LENGTH_SHORT).show()
                }
            }
            binding.removeFromCart -> {
                product?.let { prod ->
                    viewModel.deleteCartById(prod.id)
                    Toast.makeText(requireContext(), "Item Removed from Cart!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(requireContext(), "Product not loaded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}