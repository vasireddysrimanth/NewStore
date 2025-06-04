package com.dev.storeapp.presentation.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.common.asResult
import com.dev.storeapp.data.local.entity.OrderEntity
import com.dev.storeapp.domain.useCase.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) :ViewModel(){

    private val _orderDetails = MutableStateFlow<Result<List<OrderEntity>>>(Result.Loading)
    val orderDetails = _orderDetails.asStateFlow()


    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        viewModelScope.launch {
            orderUseCase.getAllOrders()
                .asResult()
                .collect{
                    _orderDetails.value = it
                }
        }
    }


    //insert product
    fun insertOrder(order: OrderEntity) {
        viewModelScope.launch {
            orderUseCase.insertOrder(order)
        }
    }


}