package com.dev.storeapp.presentation.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.data.model.User
import com.dev.storeapp.databinding.FragmentUserDetailsBinding


class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentUserDetailsBinding {
        return FragmentUserDetailsBinding.inflate(inflater,container,false)
    }
    companion object{
        const val TAG = "UserDetailsFragment"
        fun newInstance(user: User): UserDetailsFragment{
            val args = Bundle()
            args.putParcelable("user",user)
            val fragment = UserDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.productDetailToolbar.setOnClickListener {
            goBack()
            goBack()
        }
        arguments?.let {
            val user = it.getParcelable<User>("user")
            binding.firstNameText.text = user?.name?.firstname
            binding.lastNameText.text = user?.name?.lastname
            binding.emailText.text=user?.email
            binding.phoneText.text =user?.phone
            binding.usernameText.text =user?.username
            binding.addressText.text = user?.address.toString()

            //update title
            binding.toolbarTitle.text = "${user?.name?.firstname ?: ""} ${user?.name?.lastname ?: ""}".trim()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()

    }

}