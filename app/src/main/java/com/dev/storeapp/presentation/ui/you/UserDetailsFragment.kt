package com.dev.storeapp.presentation.ui.you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dev.storeapp.R
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.app.utils.toFormattedDate
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.databinding.FragmentUserDetailsBinding
import com.dev.storeapp.presentation.ui.dialog.ProfileEditBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private val viewModel: UsersViewModel by viewModels()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentUserDetailsBinding.inflate(inflater, container, false)

    companion object {
        const val TAG = "UserDetailsFragment"
        fun newInstance() = UserDetailsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        launchAndRepeatOnStarted {
            viewModel.userUiState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoader()
                        AppLogger.d(TAG, "Loading user details...")
                    }
                    is Result.Success -> {
                        hideLoader()
                        updateUI(result.data)
                        AppLogger.d(TAG, "Success loading user details.")
                        Glide.with(requireContext())
                            .load(result.data.profileImagePath?.let { File(it) })
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(binding.profileImage)
                    }
                    is Result.Error -> {
                        AppLogger.e(TAG, "Error loading user details: ${result.exception?.message}")
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.editIcon.setOnClickListener{
            val dialog = ProfileEditBottomSheetFragment.newInstance()
            dialog.show(parentFragmentManager,dialog.tag)
        }
    }

    private fun updateUI(user: FireBaseUserEntity?) = user?.let {
        binding.textViewUid.text = it.uid
        binding.textViewUserName.text = it.username
        binding.textViewEmail.text = it.email
        binding.textViewGender.text = it.gender
        binding.textViewTimestamp.text = it.createdAt.toFormattedDate()
    } ?: AppLogger.e(TAG, "User data is null!")
}