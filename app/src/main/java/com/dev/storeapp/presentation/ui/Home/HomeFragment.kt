package com.dev.storeapp.presentation.ui.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.constants.Constants
import com.dev.storeapp.databinding.FragmentHomeBinding
import com.dev.storeapp.presentation.ui.settings.SettingsFragment
import com.dev.storeapp.presentation.ui.products.ProductFragment
import com.dev.storeapp.presentation.utils.SharedPreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    companion object {
        val TAG = "HomeFragment"
        fun newInstance() = HomeFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("HomeFragment", "user email ${sharedPreferenceHelper.get(Constants.LOGGED_IN_USER_EMAIL)}")
        initViews()
        createObserver()
    }

    private fun createObserver() {
    }

    private fun initViews() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(R.id.fragment_container,ProductFragment.newInstance())
                    true
                }

//                R.id.users -> {
//                    replaceFragment(R.id.fragment_container,UserFragment.newInstance())
//                    true
//                }

                R.id.settings -> {
                    replaceFragment(R.id.fragment_container, SettingsFragment.newInstance())
                    true
                }

                else -> false


            }
        }
        binding.bottomNavigation.selectedItemId = R.id.home
    }
}

