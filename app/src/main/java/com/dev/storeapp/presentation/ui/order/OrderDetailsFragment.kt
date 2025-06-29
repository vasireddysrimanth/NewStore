package com.dev.storeapp.presentation.ui.order

import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AlignmentSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.databinding.FragmentOrderDetailsBinding
import com.dev.storeapp.presentation.ui.products.ProductFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    private var order: List<Product> = emptyList()
    private var orderId: String = ""
    private var orderDate: Long = System.currentTimeMillis()

    companion object {
        private const val ARG_PRODUCTS = "products"
        private const val ARG_ORDER_ID = "order_id"
        private const val ARG_ORDER_DATE = "order_date"

        fun newInstance(products: List<Product>): OrderDetailsFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARG_PRODUCTS, ArrayList(products))
            args.putString(ARG_ORDER_ID, generateOrderId())
            args.putLong(ARG_ORDER_DATE, System.currentTimeMillis())
            val fragment = OrderDetailsFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(products: List<Product>, orderId: String, orderDate: Long): OrderDetailsFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARG_PRODUCTS, ArrayList(products))
            args.putString(ARG_ORDER_ID, orderId)
            args.putLong(ARG_ORDER_DATE, orderDate)
            val fragment = OrderDetailsFragment()
            fragment.arguments = args
            return fragment
        }

        private fun generateOrderId(): String {
            return "ORD${System.currentTimeMillis()}${(1000..9999).random()}"
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentOrderDetailsBinding {
        return FragmentOrderDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the products list from arguments
        arguments?.let {
            order = it.getParcelableArrayList(ARG_PRODUCTS) ?: emptyList()
            orderId = it.getString(ARG_ORDER_ID, generateOrderId())
            orderDate = it.getLong(ARG_ORDER_DATE, System.currentTimeMillis())
        }

        if (order.isNotEmpty()) {
            displayReceipt(order)
        } else {
            binding.receiptTextView.text = getString(R.string.order_details_not_available)
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonGoHome.setOnClickListener {
            // Navigate back to products/home
            replaceFragment(R.id.fragment_container, ProductFragment.newInstance(), true)
        }
    }

    private fun displayReceipt(order: List<Product>) {
        val sb = SpannableStringBuilder()

        // Centered Store Name (bold)
        val storeName = "S Store\n"
        val storeNameStart = sb.length
        sb.append(storeName)
        sb.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), storeNameStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(StyleSpan(Typeface.BOLD), storeNameStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Centered Store Address
        val storeAddress = "Laxmi puram circle\nTirupati 517501, AndhraPradesh India\n"
        val addressStart = sb.length
        sb.append(storeAddress)
        sb.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), addressStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Centered Contact
        val contact = "Contact: +1 234 567 890\n"
        val contactStart = sb.length
        sb.append(contact)
        sb.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), contactStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("----------------------------------\n")

        // Order Info
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val orderInfo = "Order ID: $orderId\nDate: ${dateFormat.format(Date(orderDate))}\n"
        val orderInfoStart = sb.length
        sb.append(orderInfo)
        sb.setSpan(StyleSpan(Typeface.BOLD), orderInfoStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("----------------------------------\n")

        // Header line bold
        val header = String.format("%-15s %5s %10s\n", "Item", "Qty", "Price")
        val headerStart = sb.length
        sb.append(header)
        sb.setSpan(StyleSpan(Typeface.BOLD), headerStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("----------------------------------\n")

        var total = 0.0
        var totalItems = 0

        // Items
        for (product in order) {
            val price = product.price.toDouble() * product.quantity
            total += price
            totalItems += product.quantity
            sb.append(String.format("%-15s %5d %10.2f\n",
                product.title.take(15),
                product.quantity,
                price))
        }

        sb.append("----------------------------------\n")

        // Summary
        sb.append(String.format("Total Items: %d\n", totalItems))

        // Subtotal, tax, and total
        val subtotal = total
        val tax = total * 0.18 // 18% GST
        val grandTotal = subtotal + tax

        sb.append(String.format("Subtotal: %10.2f\n", subtotal))
        sb.append(String.format("GST (18%%): %9.2f\n", tax))
        sb.append("----------------------------------\n")

        // Grand Total line bold
        val totalLine = String.format("Grand Total: ₹%.2f\n", grandTotal)
        val totalStart = sb.length
        sb.append(totalLine)
        sb.setSpan(StyleSpan(Typeface.BOLD), totalStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("\n")

        // Payment Status
        val paymentStatus = "Payment Status: COMPLETED\nPayment Method: Cash\n"
        val paymentStart = sb.length
        sb.append(paymentStatus)
        sb.setSpan(StyleSpan(Typeface.BOLD), paymentStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("\n")

        // Centered thank you footer
        val thankYou = "Thank you for shopping with us!\nVisit Again Soon!\n"
        val thankYouStart = sb.length
        sb.append(thankYou)
        sb.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), thankYouStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Use monospace for alignment
        binding.receiptTextView.typeface = Typeface.MONOSPACE
        binding.receiptTextView.text = sb
    }

    private fun shareReceipt() {
        // Create shareable text content
        val shareText = buildString {
            append("S Store Receipt\n")
            append("Order ID: $orderId\n")
            append("Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(orderDate))}\n\n")

            var total = 0.0
            order.forEach { product ->
                val price = product.price.toDouble() * product.quantity
                total += price
                append("${product.title} x${product.quantity} - ₹${"%.2f".format(price)}\n")
            }

            val tax = total * 0.5
            val grandTotal = total + tax

            append("\nSubtotal: ₹${"%.2f".format(total)}")
            append("\nGST (5%): ₹${"%.2f".format(tax)}")
            append("\nGrand Total: ₹${"%.2f".format(grandTotal)}")
            append("\n\nThank you for shopping with S Store!")
        }

        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            putExtra(android.content.Intent.EXTRA_SUBJECT, "S Store Receipt - Order $orderId")
        }

        startActivity(android.content.Intent.createChooser(shareIntent, "Share Receipt"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}