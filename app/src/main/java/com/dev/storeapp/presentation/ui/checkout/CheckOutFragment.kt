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
import com.dev.storeapp.app.utils.AppUtils
import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.data.model.mapToPaymentProducts
import com.dev.storeapp.data.model.mapToStoreProducts
import com.dev.storeapp.databinding.FragmentCheckOutBinding
import com.dev.storeapp.presentation.adapter.CheckOutAdapter
import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
import com.dev.storeapp.presentation.ui.order.OrderDetailsFragment
import com.dev.storeapp.presentation.ui.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(), OnPaymentConfirmedListener {

    private val cartViewModel : AddToCartViewModel by viewModels()

    private var product: Product? = null
    private var allProducts: List<Product> = emptyList()
    private var checkOutAdapter: CheckOutAdapter? = null
    private val orderViewModel :OrderViewModel by viewModels()
    private var currentProductList = mutableListOf<Product>()

    companion object {
        fun newInstance() = CheckOutFragment()

        fun newInstance(product: Product): CheckOutFragment {
            return CheckOutFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("product", product)
                }
            }
        }

        fun newInstance(products: List<Product>,isAllProduct : Boolean = false): CheckOutFragment {
            return CheckOutFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("products", ArrayList(products))
                    putBoolean("isAllProduct", isAllProduct)
                }
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

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentCheckOutBinding {
        return FragmentCheckOutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkOutAdapter = CheckOutAdapter { updatedList ->
            currentProductList = updatedList.toMutableList()
            updateTotalAmount(currentProductList)
        }

        binding.recyclerView.adapter = checkOutAdapter

        val itemsToDisplay = if (allProducts.isNotEmpty()) allProducts else product?.let { listOf(it) } ?: emptyList()
        currentProductList = itemsToDisplay.toMutableList()
        checkOutAdapter?.differ?.submitList(currentProductList.toList())
        updateTotalAmount(currentProductList)

        binding.checkoutToolbarBackButton.setOnClickListener {
            goBack()
            goBack()
        }

        binding.confirmButton.setOnClickListener {
            navigateToPayment()
        }

    }


    private fun navigateToPayment() {
        if (currentProductList.isEmpty()) {
            showSnackBar("No items to checkout", binding.root)
            return
        }
        val paymentProducts = mapToPaymentProducts(currentProductList)
        val paymentFragment = PaymentNewFragment.newInstance(paymentProducts)
        paymentFragment.setOnPaymentConfirmedListener(this)
        replaceFragment(R.id.fragment_container, paymentFragment, true)
    }

    override fun onPaymentConfirmed(products: List<com.dev.payment.payment.Product>) {
        val storeProducts = mapToStoreProducts(products)
        arguments?.getBoolean("isAllProduct", false).let {
            if(it == true) cartViewModel.deleteAllCarts()
            else cartViewModel.deleteCartById(product?.id ?: -1)
        }
        replaceFragment(R.id.fragment_container,OrderDetailsFragment.newInstance(storeProducts), true)
        orderViewModel.insertOrder(OrderEntity(
            products = storeProducts,
            orderId = AppUtils.getGuid(),
            totalAmount = storeProducts.sumOf { it.price.toDouble() * it.quantity },
            timestamp = System.currentTimeMillis(),
            paymentMode = "Cash",
        ))

    }


    private fun updateTotalAmount(products: List<Product>) {
        val total = products.sumOf { it.price.toDouble() * it.quantity }
        binding.totalAmount.text = "₹%.2f".format(total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        checkOutAdapter = null
    }
}