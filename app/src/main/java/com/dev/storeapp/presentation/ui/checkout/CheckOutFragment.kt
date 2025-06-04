package com.dev.storeapp.presentation.ui.checkout

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.data.model.Product
import com.dev.storeapp.databinding.FragmentCheckOutBinding
import com.dev.storeapp.presentation.adapter.CheckOutAdapter
import com.dev.storeapp.presentation.ui.payment.PaymentFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>() {

    private var product: Product? = null
    private var allProducts: List<Product> = emptyList()
    private var checkOutAdapter: CheckOutAdapter? = null

    // Mutable list to keep the latest product list (edited, deleted)
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

        fun newInstance(products: List<Product>): CheckOutFragment {
            return CheckOutFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("products", ArrayList(products))
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

        checkOutAdapter = CheckOutAdapter()
        binding.recyclerView.adapter = checkOutAdapter

        // Initialize currentProductList with initial data
        val itemsToDisplay = if (allProducts.isNotEmpty()) allProducts else product?.let { listOf(it) } ?: emptyList()
        currentProductList = itemsToDisplay.toMutableList()

        // Submit list to adapter
        checkOutAdapter?.differ?.submitList(currentProductList.toList())

        // Update total amount on UI
        updateTotalAmount(currentProductList)

        // Back button listener
        binding.checkoutToolbarBackButton.setOnClickListener {
            goBack()
        }

        // Confirm button listener: pass currentProductList to PaymentFragment
        binding.confirmButton.setOnClickListener {
            // *** HERE IS THE FIX: Pass the currentProductList which is updated ***
            replaceFragment(
                R.id.fragment_container,
                PaymentFragment.newInstance(currentProductList),
                true
            )
        }

        attachSwipeGestures()
    }

    private fun attachSwipeGestures() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Remove item from currentProductList and update adapter + UI
                        if (position >= 0 && position < currentProductList.size) {
                            currentProductList.removeAt(position)
                            checkOutAdapter?.differ?.submitList(currentProductList.toList())
                            updateTotalAmount(currentProductList)
                            Snackbar.make(binding.root, "Item Deleted", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        // Edit quantity for the item
                        if (position >= 0 && position < currentProductList.size) {
                            val selectedProduct = currentProductList[position]
                            // Assume DialogUtils.showQuantityDialog takes context, current qty, and returns new qty in callback
                            DialogUtils.showQuantityDialog(requireContext(), selectedProduct.quantity) { newQty ->
                                val updatedProduct = selectedProduct.copy(quantity = newQty)
                                currentProductList[position] = updatedProduct
                                checkOutAdapter?.differ?.submitList(currentProductList.toList())
                                updateTotalAmount(currentProductList)
                            }
                        }
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .addSwipeLeftBackgroundColor(Color.RED)
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .setSwipeLeftActionIconTint(Color.WHITE)
                    .addSwipeRightLabel("Edit")
                    .addSwipeRightBackgroundColor(Color.GREEN)
                    .addSwipeRightActionIcon(R.drawable.outline_landscape_2_edit_24)
                    .setSwipeRightLabelColor(Color.WHITE)
                    .setSwipeRightActionIconTint(Color.WHITE)
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerView)
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