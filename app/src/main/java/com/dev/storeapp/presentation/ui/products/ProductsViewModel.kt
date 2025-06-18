package com.dev.storeapp.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.domain.useCase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
)  :ViewModel() {

    //create a state for products
    private val _uiState = MutableStateFlow<DataStatus<List<ProductEntity>>>(DataStatus.empty())
    var uiState = _uiState.asStateFlow()

    private val _productIdUiState = MutableStateFlow<Result<ProductEntity>>(Result.Loading)
    val productIdUiState = _productIdUiState.asStateFlow()

    //create professional way to flow with state Data using result


    init{
        getAllProducts()
    }


    //get all products
    private fun getAllProducts() = viewModelScope.launch {
        _uiState.value = DataStatus.loading()
        productUseCase.getAllProducts()
            .catch {  _uiState.value = it.message?.let { error ->  DataStatus.error(error) }!!}
            .collect{ _uiState.value =  DataStatus.success(it)}
    }

    //delete product
    suspend fun deleteProduct(productEntity: ProductEntity) {
        productUseCase.deleteProduct(productEntity)
    }

    //delete product by id
    suspend fun deleteProductById(id: Int) {
        viewModelScope.launch {
            productUseCase.deleteProductById(id)
        }
    }

    //insert product
     fun insertProduct(productEntity: ProductEntity) {
         viewModelScope.launch {
             productUseCase.insertProduct(productEntity)
         }
    }


    //get product by id
    fun getProductById(id: Int) = viewModelScope.launch {
        _productIdUiState.value = Result.Loading
        productUseCase.getProductById(id)
            .catch { _productIdUiState.value = Result.Error(it) }
            .collect { product ->
                if (product != null) {
                    _productIdUiState.value = Result.Success(product)
                } else {
                    _productIdUiState.value = Result.Error(Exception("Product not found"))
                }
            }

    }

    fun createProductToServer(productEntity: ProductEntity){
        viewModelScope.launch {
            productUseCase.createProductToServer(productEntity)
        }
    }




}