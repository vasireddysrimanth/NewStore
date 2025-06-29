package com.dev.storeapp.presentation.ui.carts

import androidx.lifecycle.ViewModel
import com.dev.storeapp.data.local.entity.AddToCartEntity
import com.dev.storeapp.domain.useCase.AddToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.common.asResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase
) :ViewModel(){

    private val _uiState  = MutableStateFlow<Result<List<AddToCartEntity>>>(Result.Loading)
    var uiState = _uiState.asStateFlow()

    private val _isInCart = MutableStateFlow(false)
    var isInCart = _isInCart.asStateFlow()

    init {
        getAllCarts()
    }

    fun insertToCart(addToCartEntity: AddToCartEntity) {
        viewModelScope.launch {
            addToCartUseCase.insertToCart(addToCartEntity)
            _isInCart.value = true
            // Refresh the cart list after insert
            getAllCarts()
        }
    }

    fun upsertToCart(addToCartEntity: AddToCartEntity) {
        viewModelScope.launch {
            addToCartUseCase.upsertToCart(addToCartEntity)
            _isInCart.value = true
            // Refresh the cart list after upsert - THIS WAS MISSING
            getAllCarts()
        }
    }

    private fun getAllCarts() {
        viewModelScope.launch {
            addToCartUseCase.getAllCarts()
                .asResult()
                .collect{
                    _uiState.value = it
                }
        }
    }

    fun deleteAllCarts() {
        viewModelScope.launch {
            addToCartUseCase.deleteAllCarts()
            getAllCarts()
        }
    }

    fun deleteCartById(id: Int) {
        viewModelScope.launch {
            addToCartUseCase.deleteCartById(id)
            _isInCart.value = false
            // Refresh the cart list after delete
            getAllCarts()
        }
    }

    fun isProductInCart(productId: Int) {
        viewModelScope.launch {
            _isInCart.value = addToCartUseCase.isProductInCart(productId)
        }
    }
}