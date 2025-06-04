package com.dev.storeapp.presentation.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.databinding.FragmentOrderBinding
import com.dev.storeapp.presentation.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>() {

    private val viewModel by viewModels<OrderViewModel>()
    private  var adapter : OrderAdapter? = null

    companion object {
        const val TAG = "OrderFragment"
        fun newInstance() = OrderFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrderAdapter()
        initViews()
        createObserver()
    }

    private fun initViews() {

        binding.apply {
            recyclerView.adapter = adapter
            toolbar.setOnClickListener {
                goBack()
                goBack()
            }
        }
    }

    private fun createObserver() {
        launchAndRepeatOnStarted {
            viewModel.orderDetails.collect {
                when(it){
                    is Result.Loading  ->{
                        AppLogger.d(TAG,"Loading...!")
                    }
                    is Result.Success -> {
                        Log.i("OrderFragment", "Orders: ${it.data}")
                        adapter?.differ?.submitList(it.data)
                    }
                    is Result.Error -> {
                        AppLogger.e(TAG,"Error: ${it.exception}")
                    }
                }
            }
        }
    }


}


