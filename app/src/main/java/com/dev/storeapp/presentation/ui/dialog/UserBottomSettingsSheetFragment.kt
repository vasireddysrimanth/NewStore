package com.dev.storeapp.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.dev.storeapp.R
import com.dev.storeapp.app.constants.Constants
import com.dev.storeapp.databinding.FragmentUserBottomSettingsSheetBinding
import com.dev.storeapp.presentation.ui.you.UsersViewModel
import com.dev.storeapp.presentation.ui.you.ProfileFragment
import com.dev.storeapp.presentation.ui.you.SettingsDetailFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserBottomSettingsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserBottomSettingsSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels()

    companion object {
        fun newInstance() = UserBottomSettingsSheetFragment()
        const val TAG = "UserBottomSettingsSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBottomSettingsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.clearButton.setOnClickListener { dismiss() }
        binding.notificationTextView.setOnClickListener {
            openSettingsDetailFragment(Constants.SECTION_NOTIFICATIONS)
        }
        binding.permissionsTextView.setOnClickListener {
            openSettingsDetailFragment(Constants.SECTION_PERMISSIONS)
        }
        binding.defaultPurchaseSettingsTextView.setOnClickListener {
            openSettingsDetailFragment(Constants.SECTION_PURCHASE)
        }
        binding.legalAndAboutTextView.setOnClickListener {
            openSettingsDetailFragment(Constants.SECTION_ABOUT)
        }
    }

    private fun openSettingsDetailFragment(section: String) {
        dismiss()
        activity?.supportFragmentManager?.commit {
            replace(R.id.fragment_container, SettingsDetailFragment.newInstance(section))
            addToBackStack(ProfileFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}