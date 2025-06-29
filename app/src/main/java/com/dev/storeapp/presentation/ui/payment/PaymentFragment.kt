//package com.dev.storeapp.presentation.ui.payment
//
//import DialogUtils
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.viewModels
//import com.dev.storeapp.R
//import com.dev.storeapp.app.base.BaseFragment
//import com.dev.storeapp.app.utils.AppUtils
//import com.dev.storeapp.data.local.entity.OrderEntity
//import com.dev.storeapp.data.model.Product
//import com.dev.storeapp.databinding.FragmentPaymentBinding
//import com.dev.storeapp.presentation.ui.carts.AddToCartViewModel
//import com.dev.storeapp.presentation.ui.order.OrderDetailsFragment
//import com.dev.storeapp.presentation.ui.order.OrderViewModel
//import com.google.android.material.snackbar.Snackbar
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {
//
//    private var productList: List<Product> = emptyList()
//    private val orderViewModel by viewModels<OrderViewModel>()
//    private val cartViewModel by viewModels<AddToCartViewModel>()
//    private var paymentMode: String? = null
//
//    companion object {
//        fun newInstance(): PaymentFragment {
//            return PaymentFragment()
//        }
//
//        fun newInstance(products: List<Product>): PaymentFragment {
//            val args = Bundle()
//            args.putParcelableArrayList("products", ArrayList(products))
//            val fragment = PaymentFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//    override fun getBinding(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        bundle: Bundle?
//    ): FragmentPaymentBinding {
//        return FragmentPaymentBinding.inflate(inflater, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        arguments?.let {
//            productList = it.getParcelableArrayList("products") ?: emptyList()
//        }
//        initViews()
//    }
//
//    private fun initViews() {
//        // Fixed: Calculate total amount considering quantity
//        binding.totalAmount.text = "₹%.2f".format(productList.sumOf { it.price.toDouble() * it.quantity })
//
//        binding.cardButton.setOnClickListener {
//            paymentMode = "Card"
//            DialogUtils.showCommonDialog(
//                requireContext(),
//                title = "Confirm Payment",
//                message = "Are you sure you want to proceed with card payment?",
//                onYes = {
////                    binding.confirmPaymentButton.visibility = View.VISIBLE
//                    showSnackBar(
//                        "Card payment is not implemented yet.",
//                        contextView = binding.root
//                    )
//                },
//                onNo = {
//                    binding.confirmPaymentButton.visibility = View.GONE
//                }
//            )
//        }
//
//        binding.cashButton.setOnClickListener {
//            paymentMode = "Cash"
//            DialogUtils.showCommonDialog(
//                requireContext(),
//                title = "Confirm Payment",
//                message = "Are you sure you want to proceed with cash payment?",
//                onYes = {
//                    binding.confirmPaymentButton.visibility = View.VISIBLE
//                },
//                onNo = {
//                    binding.confirmPaymentButton.visibility = View.GONE
//                }
//            )
//        }
//
//        binding.confirmPaymentButton.setOnClickListener {
//            createOrder()
//            cartViewModel.deleteAllCarts()
//            replaceFragment(R.id.fragment_container, OrderDetailsFragment.newInstance(productList), true)
//        }
//    }
//
//    private fun createOrder() {
//        val orderEntity = OrderEntity(
//            orderId = AppUtils.getGuid(),
//            products = productList,
//            totalAmount = productList.sumOf { it.price.toDouble() * it.quantity },
//            timestamp = System.currentTimeMillis(),
//            paymentMode = paymentMode
//        )
//        orderViewModel.insertOrder(orderEntity)
//        goBack()
//    }
//}