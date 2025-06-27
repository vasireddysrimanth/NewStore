package com.dev.storeapp.presentation.ui.you

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
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
    private val userUseCase: UserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataStatus<List<UserEntity>>>(DataStatus.empty())
    val uiState = _uiState.asStateFlow()

    private val _userDetailsUiState = MutableStateFlow<Result<UserEntity>>(Result.Loading)
    val userDetailsUiState = _userDetailsUiState.asStateFlow()

    private val _userUiState = MutableStateFlow<Result<FireBaseUserEntity>>(Result.Loading)
    val userUiState = _userUiState.asStateFlow()

    val userName = MutableStateFlow("")

    init {
        getAllUsers()
        getUserByEmail()
    }

    private fun getAllUsers() = viewModelScope.launch {
        _uiState.value = DataStatus.loading()
        userUseCase.getAllUsers()
            .catch { _uiState.value = DataStatus.error(it.message ?: "Unknown error") }
            .collect { _uiState.value = DataStatus.success(it) }
    }

    fun getUserByUsername(username: String) = viewModelScope.launch {
        userUseCase.getUserByUsername(username)
    }

    fun deleteAllUsers() = viewModelScope.launch {
        userUseCase.deleteAllUsers()
    }

    fun deleteUserByUsername(username: String) = viewModelScope.launch {
        userUseCase.deleteUserByUsername(username)
    }

    fun getUserById(id: Int) = viewModelScope.launch {
        _userDetailsUiState.value = Result.Loading
        userUseCase.getUserById(id)
            .catch { _userDetailsUiState.value = Result.Error(it) }
            .collect { user -> _userDetailsUiState.value = Result.Success(user) }
    }

    fun insertUser(user: UserEntity) = viewModelScope.launch {
        userUseCase.insertUser(user)
    }

    fun loadUserName() = viewModelScope.launch {
        userName.value = userUseCase.getUserNameByEmail().toString()
    }

    private fun getUserByEmail() = viewModelScope.launch {
        _userUiState.value = Result.Loading
        try {
            userUseCase.getUserByEmail().collect { user ->
                _userUiState.value = Result.Success(user)
            }
        } catch (e: Exception) {
            _userUiState.value = Result.Error(e)
        }
    }

     fun upsertUser(user: FireBaseUserEntity) {
        viewModelScope.launch {
            Log.e("userImagePath","userImage ${user.profileImagePath}")
            userUseCase.upsertUser(user)
        }
    }
}