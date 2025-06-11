package com.dev.storeapp.presentation.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private val viewModel: UsersViewModel by viewModels()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentUserDetailsBinding {
        return FragmentUserDetailsBinding.inflate(inflater, container, false)
    }

    companion object {
        const val TAG = "UserDetailsFragment"
        fun newInstance(userId: Int) = UserDetailsFragment().apply {
            arguments = bundleOf("userId" to userId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        createObserver()
    }

    private fun initViews() {
        binding.productDetailToolbar.setOnClickListener {
            goBack()
        }

        arguments?.getInt("userId", -1)?.let { userId ->
            viewModel.getUserById(userId)
        } ?: run {
            AppLogger.e(TAG, "Invalid or missing user ID.")
        }
    }


    private fun createObserver() {
        launchAndRepeatOnStarted {
            viewModel.userDetailsUiState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        AppLogger.d("UserDetailsFragment", "Loading user details...")
                    }
                    is Result.Success -> {
                        val user = result.data
                        updateUI(user)
                    }
                    is Result.Error -> {
                        AppLogger.e("UserDetailsFragment", "Error loading user details: ${result.exception?.message}")
                    }
                }
            }
        }
    }

    private fun updateUI(user: UserEntity?) {
        if (user != null) {
            binding.firstNameText.text = user.name.firstname
            binding.lastNameText.text = user.name.lastname
            binding.emailText.text = user.email
            binding.phoneText.text = user.phone
            binding.usernameText.text = user.username
            binding.addressText.text = user.address.toString()
            binding.toolbarTitle.text = "${user.name.firstname} ${user.name.lastname}".trim()
        } else {
            AppLogger.e(TAG, "User data is null!")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
