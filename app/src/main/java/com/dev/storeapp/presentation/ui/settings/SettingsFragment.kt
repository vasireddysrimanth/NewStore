package com.dev.storeapp.presentation.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.databinding.FragmentSettingsBinding
import com.dev.storeapp.presentation.auth.LoginActivity
import com.dev.storeapp.presentation.ui.carts.CartsFragment
import com.dev.storeapp.presentation.ui.order.OrderFragment
import com.dev.storeapp.presentation.ui.users.UserFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(),View.OnClickListener {

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsFragment()
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ) =  FragmentSettingsBinding.inflate(inflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }

    private fun initViews(){
        binding.cardUsers.setOnClickListener(this)
        binding.cardCartItems.setOnClickListener(this)
        binding.cardOrders.setOnClickListener(this)
        binding.cardLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.cardUsers -> replaceFragment(R.id.fragment_container,UserFragment.newInstance(),true)
            binding.cardCartItems -> replaceFragment(R.id.fragment_container,CartsFragment.newInstance(),true)
            binding.cardOrders -> {replaceFragment(R.id.fragment_container,OrderFragment.newInstance(),true)}
            binding.cardLogout -> {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}


