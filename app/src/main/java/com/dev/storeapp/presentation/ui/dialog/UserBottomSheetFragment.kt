package com.dev.storeapp.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dev.storeapp.R
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.databinding.FragmentUserBottomSheetBinding
import com.dev.storeapp.presentation.ui.auth.LoginFragment
import com.dev.storeapp.presentation.ui.you.UserDetailsFragment
import com.dev.storeapp.presentation.ui.you.UsersViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class UserBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserBottomSheetBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = UserBottomSheetFragment()
        const val TAG = "UserBottomSheetFragment"
    }

    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        launchAndRepeatOnStarted {
            viewModel.userUiState.collect {
                when (it) {
                    is Result.Loading -> AppLogger.d(TAG, "Loading user info...")
                    is Result.Success -> {
                        binding.textViewUserName.text = it.data.username
                        binding.textViewSignedAsFull.text = "Signed in as: ${it.data.email}"
                        Glide.with(requireContext())
                            .load(it.data.profileImagePath?.let { File(it) })
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(binding.productImage)
                    }
                    is Result.Error -> AppLogger.e(TAG, "Error: ${it.exception}")
                }
            }
        }
    }

    private fun initViews() {
        binding.signOut.setOnClickListener {
            dismiss()
            replaceFragment(R.id.fragment_container, LoginFragment.newInstance(), false)
        }
        binding.clearButton.setOnClickListener { dismiss() }
        binding.textViewView.setOnClickListener {
            dismiss()
            replaceFragment(R.id.fragment_container, UserDetailsFragment.newInstance(), false)
        }
    }

    private fun replaceFragment(
        containerId: Int,
        fragment: Fragment,
        addToBackStack: Boolean = false,
        tag: String? = null
    ) {
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            replace(containerId, fragment, tag)
            if (addToBackStack) addToBackStack(tag)
        }
    }
}