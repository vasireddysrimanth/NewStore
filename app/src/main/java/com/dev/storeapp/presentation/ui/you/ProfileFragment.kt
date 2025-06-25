package com.dev.storeapp.presentation.ui.you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.model.AccountFeature
import com.dev.storeapp.data.model.FeatureDestination
import com.dev.storeapp.databinding.FragmentYouBinding
import com.dev.storeapp.presentation.adapter.AccountFeaturesAdapter
import com.dev.storeapp.presentation.ui.carts.CartsFragment
import com.dev.storeapp.presentation.ui.order.OrderFragment
import com.dev.storeapp.presentation.ui.products.ProductFragment
import com.dev.storeapp.presentation.ui.users.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentYouBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
        fun newInstance() = ProfileFragment()
    }

    private val userViewModel: UsersViewModel by viewModels()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentYouBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.loadUserName()
        setupObservers()
        setupViews()
    }

    private fun setupObservers() {
        launchAndRepeatOnStarted {
            userViewModel.userUiState.collect { user->
                when(user){
                    is Result.Error -> {
                        AppLogger.e(TAG,"Loading User Data..!")
                    }
                    is Result.Loading -> {
                        showLoader()
                    }
                    is Result.Success -> {
                        hideLoader()
                        AppLogger.e("userDetails", " 1 ,${user.data.username}")
                        if (user.data.username.isNotEmpty()) {
                            binding.textViewUserName.text = user.data.username
                            Glide.with(requireContext())
                                .load(user.data.profileImagePath?.let { File(it) })
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(binding.productImage)
                        }
                    }
                }

            }
        }
    }

    private fun setupViews() {
        val features = listOf(
            AccountFeature("Buy Again", FeatureDestination.BUY_AGAIN),
            AccountFeature("Recent Orders", FeatureDestination.RECENT_ORDERS),
            AccountFeature("Your Profile", FeatureDestination.PROFILE),
            AccountFeature("Orders", FeatureDestination.ORDERS),
            AccountFeature("Carts", FeatureDestination.CARTS)
        )

        val adapter = AccountFeaturesAdapter(features, ::handleOnClick)
        binding.accountRecyclerView.adapter = adapter

        binding.textViewUserName.setOnClickListener {
            UserBottomSheetFragment.newInstance()
                .show(parentFragmentManager, UserBottomSheetFragment.TAG)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.submitList(
                    if (newText.isNullOrEmpty()) features
                    else features.filter { it.title.contains(newText, ignoreCase = true) }
                )
                return true
            }
        })

        binding.orderButton.setOnClickListener {
            replaceFragment(R.id.fragment_container, OrderFragment.newInstance(), true)
        }
        binding.cartsButton.setOnClickListener {
            replaceFragment(R.id.fragment_container, CartsFragment.newInstance(), true)
        }
    }

    private fun handleOnClick(destination: FeatureDestination) {
        when (destination) {
            FeatureDestination.BUY_AGAIN -> replaceFragment(R.id.fragment_container, ProductFragment.newInstance(),true)
            FeatureDestination.RECENT_ORDERS-> replaceFragment(R.id.fragment_container, ProductFragment.newInstance(),true)
            FeatureDestination.ORDERS -> replaceFragment(R.id.fragment_container, OrderFragment.newInstance(),true)
            FeatureDestination.PROFILE -> replaceFragment(R.id.fragment_container, UserBottomSheetFragment.newInstance(),true)
            FeatureDestination.CARTS -> replaceFragment(R.id.fragment_container, CartsFragment.newInstance(),true)
        }
    }
}