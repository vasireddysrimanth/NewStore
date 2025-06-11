package com.dev.storeapp.presentation.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.common.DataStatus
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.data.local.entity.toUser
import com.dev.storeapp.data.model.User
import com.dev.storeapp.databinding.FragmentUserBinding
import com.dev.storeapp.presentation.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>() {
    private val viewModel  by viewModels<UsersViewModel>()
    private val adapter = UserAdapter()

    // create a list for users
    private var userList = listOf<UserEntity>()

    companion object {
        val TAG = "UserFragment"
        fun newInstance() = UserFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        createObserver()
    }

    private fun initViews() {
        binding.productRecyclerView.adapter = adapter
        adapter.setOnItemClickListener { userEntity ->
            goToUserDetailsFragment(userEntity)
        }
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = if (newText.isNullOrEmpty()) {
                    userList
                } else {
                    userList.filter { user ->
                        user.username.contains(newText, ignoreCase = true)
                    }
                }
                adapter.differ.submitList(filteredList)
                return true
            }
        })

        binding.btnShowDialog.setOnClickListener{
            val addUserDialogFragment = AddUserDialogFragment.newInstance()
            addUserDialogFragment.show(childFragmentManager,AddUserDialogFragment.TAG)
        }
    }

    private fun goToUserDetailsFragment(toUser: UserEntity) {
        replaceFragment(R.id.fragment_container, UserDetailsFragment.newInstance(toUser.id), true)
    }

    private fun createObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {users->
                    when (users.status) {
                        DataStatus.Status.LOADING -> {}
                        DataStatus.Status.SUCCESS -> users.data?.let {
                            userList = it
                            adapter.differ.submitList(userList)
                        }
                        DataStatus.Status.ERROR -> showError()
                        DataStatus.Status.EMPTY -> {}
                    }

                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Something Went Wrong..!", Toast.LENGTH_SHORT).show()
    }
}
