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
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.data.model.toAddToCartEntity
import com.dev.storeapp.databinding.FragmentProductDetailsBinding
import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
import com.dev.storeapp.presentation.ui.checkout.CheckOutFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>(), View.OnClickListener {

    private val viewModel: AddToCartViewModel by viewModels()
    private var product: Product? = null

    companion object {
        const val TAG = "ProductDetailsFragment"
        fun newInstance() = ProductDetailsFragment()
        fun newInstance(product: Product): ProductDetailsFragment {
            val args = Bundle()
            args.putParcelable("product", product)
            val fragment = ProductDetailsFragment()
            fragment.arguments = args
            return fragment
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
        initViews()
        createObserver()
    }

    private fun initViews() {
        binding.productDetailToolbar.setOnClickListener(this)
        binding.buyNowButton.setOnClickListener(this)
        binding.addToCart.setOnClickListener(this)
        binding.removeFromCart.setOnClickListener(this)

        arguments.let {
            product = it?.getParcelable<Product>("product")
            binding.apply {
                titleText.text = product?.title
                brandText.text = product?.brand
                colorText.text = product?.color
                categoryText.text = product?.category
                discountText.text = product?.discount.toString()
                modelText.text = product?.model
                priceText.text = product?.price.toString()
                descriptionText.text = product?.description
                Glide.with(requireContext())
                    .load(product?.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.productImage)
            }
            binding.toolbarTitle.text = product?.title
            viewModel.isProductInCart(product?.id ?: 0)
        }
    }

    private fun createObserver(){
        launchAndRepeatOnStarted {
            viewModel.isInCart.collect{
                if (it) {
                    binding.addToCart.visibility = View.GONE
                    binding.removeFromCart.visibility = View.VISIBLE
                } else {
                    binding.addToCart.visibility = View.VISIBLE
                    binding.removeFromCart.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.productDetailToolbar -> {
                goBack()
            }
            binding.buyNowButton -> {
                replaceFragment(R.id.fragment_container,CheckOutFragment.newInstance(product!!),true)
            }
            binding.addToCart -> {
                product?.toAddToCartEntity()?.let { viewModel.insertToCart(it) }
                Toast.makeText(requireContext(), "Item Added to Cart..!", Toast.LENGTH_SHORT).show()
            }
            binding.removeFromCart -> {
                product?.toAddToCartEntity()?.let { viewModel.deleteCartById(it.id) }
                Toast.makeText(requireContext(), "Item Removed from Cart..!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}