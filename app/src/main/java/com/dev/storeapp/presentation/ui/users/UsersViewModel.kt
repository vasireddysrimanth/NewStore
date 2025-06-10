package com.dev.storeapp.presentation.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.domain.useCase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userUseCase: UserUseCase
):ViewModel() {

    //create a state for products
    private val _uiState = MutableStateFlow<DataStatus<List<UserEntity>>>(DataStatus.empty())
    var uiState = _uiState.asStateFlow()

    private val _userDetailsUiState = MutableStateFlow<Result<UserEntity>>(Result.Loading)
    var userDetailsUiState = _userDetailsUiState.asStateFlow()

    init {
        getAllUsers()
    }

    private fun getAllUsers() = viewModelScope.launch {
        _uiState.value = DataStatus.loading()
        userUseCase.getAllUsers()
            .catch {  _uiState.value = it.message?.let { error ->  DataStatus.error(error) }!! }
            .collect{ _uiState.value =  DataStatus.success(it)}
    }

    fun getUserByUsername(username: String){
        viewModelScope.launch {
            userUseCase.getUserByUsername(username)
        }
    }


    suspend fun deleteAllUsers() {
        viewModelScope.launch {
            userUseCase.deleteAllUsers()
        }
    }

    suspend fun deleteUserByUsername(username: String){
        viewModelScope.launch {
            userUseCase.deleteUserByUsername(username)
        }
    }

    fun getUserById(id: Int){
        viewModelScope.launch {
            _userDetailsUiState.value = Result.Loading
            userUseCase.getUserById(id)
                .catch { _userDetailsUiState.value = Result.Error(it) }
                .collect { user ->
                    _userDetailsUiState.value = Result.Success(user)
                }
        }
    }

    suspend fun deleteUserById(id: Int){
        viewModelScope.launch {
            userUseCase.deleteUserById(id)
        }
    }

     fun insertUser(user:UserEntity){
        viewModelScope.launch {
            userUseCase.insertUser(user)
        }
    }

    fun createUserToServer(user: UserEntity) = viewModelScope.launch {
        userUseCase.createUserToServer(user)
    }

}