package com.dev.storeapp.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dev.storeapp.app.base.BaseDialogFragment
import com.dev.storeapp.data.local.entity.Address
import com.dev.storeapp.data.local.entity.Name
import com.dev.storeapp.data.local.entity.UserEntity
import com.dev.storeapp.databinding.FragmentAddUserDialogBinding
import com.dev.storeapp.presentation.ui.you.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserDialogFragment : BaseDialogFragment<FragmentAddUserDialogBinding>() {

    private val viewModel: UsersViewModel by viewModels()

    companion object {
        val TAG = "AddUserDialogFragment"
        fun newInstance() = AddUserDialogFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddUserDialogBinding {
        return FragmentAddUserDialogBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.imgClose.setOnClickListener { dismiss() }

        binding.apply {
            btnSave.setOnClickListener {
                if (edtUserName.text.isEmpty() || edtName.text.isEmpty() || edtEmail.text.isEmpty() || edtPhone.text.isEmpty() || edtAddress.text.isEmpty()) {
                    Toast.makeText(requireContext(), "Please Fill All the Fields", Toast.LENGTH_SHORT).show()
                } else {
                    val addressParts = edtAddress.text.toString().split(",").map { it.trim() }

                    val entity = UserEntity(
                        username = edtUserName.text.toString(),
                        name = Name(
                            firstname = edtName.text.toString().split(" ").getOrNull(0) ?: "",
                            lastname = edtName.text.toString().split(" ").getOrNull(1) ?: ""
                        ),
                        email = edtEmail.text.toString(),
                        phone = edtPhone.text.toString(),
                        address = Address(
                            street = addressParts.getOrNull(0) ?: "",
                            number = addressParts.getOrNull(1) ?: "",
                            city = addressParts.getOrNull(2) ?: "",
                            zipcode = addressParts.getOrNull(3) ?: ""
                        ),
                        password = ""
                    )
                    viewModel.insertUser(entity)
                    Toast.makeText(requireContext(), "User Added Successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                    clearFocus()
                }
            }
        }
    }

    private fun clearFocus() {
        binding.apply {
            edtName.setText("")
            edtUserName.setText("")
            edtAddress.setText("")
            edtEmail.setText("")
            edtPhone.setText("")
        }
    }
}
