package com.dev.storeapp.presentation.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dev.payment.OnPaymentConfirmedListener
import com.dev.payment.payment.PaymentNewFragment
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.data.model.mapToPaymentProducts
import com.dev.storeapp.data.model.mapToStoreProducts
import com.dev.storeapp.databinding.FragmentCheckOutBinding
import com.dev.storeapp.presentation.adapter.CheckOutAdapter
import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
import com.dev.storeapp.presentation.ui.order.OrderDetailsFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(), OnPaymentConfirmedListener {

    private val cartViewModel: AddToCartViewModel by viewModels()
    private var product: Product? = null
    private var allProducts: List<Product> = emptyList()
    private var currentProductList = mutableListOf<Product>()
    private var checkOutAdapter: CheckOutAdapter? = null

    companion object {
        fun newInstance(product: Product? = null, products: List<Product>? = null) = CheckOutFragment().apply {
            arguments = Bundle().apply {
                product?.let { putParcelable("product", it) }
                products?.let { putParcelableArrayList("products", ArrayList(it)) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable("product")
            allProducts = it.getParcelableArrayList("products") ?: emptyList()
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) =
        FragmentCheckOutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupToolbar()
        setupConfirmButton()
    }

    private fun setupAdapter() {
        checkOutAdapter = CheckOutAdapter { updatedList ->
            currentProductList = updatedList.toMutableList()
            updateTotalAmount()
        }
        binding.recyclerView.adapter = checkOutAdapter
        currentProductList = (if (allProducts.isNotEmpty()) allProducts else product?.let { listOf(it) } ?: emptyList()).toMutableList()
        checkOutAdapter?.differ?.submitList(currentProductList)
        updateTotalAmount()
    }

    private fun setupToolbar() {
        binding.checkoutToolbarBackButton.setOnClickListener { goBack() }
    }

    private fun setupConfirmButton() {
        binding.confirmButton.setOnClickListener {
            if (currentProductList.isEmpty()) {
                Snackbar.make(binding.root, "No items to checkout", Snackbar.LENGTH_SHORT).show()
            } else {
                navigateToPayment()
            }
        }
    }

    private fun navigateToPayment() {
        val paymentFragment = PaymentNewFragment.newInstance(mapToPaymentProducts(currentProductList))
        paymentFragment.setOnPaymentConfirmedListener(this)
        replaceFragment(R.id.fragment_container, paymentFragment, true)
    }

    override fun onPaymentConfirmed(products: List<com.dev.payment.payment.Product>) {
        cartViewModel.deleteAllCarts()
        replaceFragment(R.id.fragment_container, OrderDetailsFragment.newInstance(mapToStoreProducts(products)), true)
    }

    private fun updateTotalAmount() {
        val total = currentProductList.sumOf { it.price.toDouble() * it.quantity }
        binding.totalAmount.text = "₹%.2f".format(total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkOutAdapter = null
    }
}