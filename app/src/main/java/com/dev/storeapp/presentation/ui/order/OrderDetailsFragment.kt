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

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    private var order: List<Product> = emptyList()

    companion object {
        private const val ARG_PRODUCTS = "products"

        fun newInstance(products: List<Product>): OrderDetailsFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARG_PRODUCTS, ArrayList(products))
            val fragment = OrderDetailsFragment()
            fragment.arguments = args
            return fragment
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
        }

        if (order.isNotEmpty()) {
            displayReceipt(order)
        } else {
            binding.receiptTextView.text = "Order details not available."
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

        // Header line bold
        val header = String.format("%-15s %5s %10s\n", "Item", "Quantity", "Price")
        val headerStart = sb.length
        sb.append(header)
        sb.setSpan(StyleSpan(Typeface.BOLD), headerStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("----------------------------------\n")

        var total = 0.0

        // Items
        for (product in order) {
            val qty = 1
            val price = product.price.toDouble() * product.quantity
            total += price
            sb.append(String.format("%-15s %5d %10.2f\n", product.title.take(15), product.quantity, price))
        }

        sb.append("----------------------------------\n")

        // Total line bold and inside lines
        val totalLine = String.format("Total: ₹%.2f\n", total)
        val totalStart = sb.length
        sb.append(totalLine)
        sb.setSpan(StyleSpan(Typeface.BOLD), totalStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        sb.append("\n")

        // Centered thank you footer
        val thankYou = "Thank you for shopping with us!\n"
        val thankYouStart = sb.length
        sb.append(thankYou)
        sb.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), thankYouStart, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Use monospace for alignment
        binding.receiptTextView.typeface = Typeface.MONOSPACE
        binding.receiptTextView.text = sb

        binding.buttonGoHome.setOnClickListener{
            replaceFragment(R.id.fragment_container, ProductFragment.newInstance(), true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

