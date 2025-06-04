package com.dev.storeapp.presentation.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.dev.storeapp.app.base.BaseDialogFragment
import com.dev.storeapp.data.local.entity.ProductEntity
import com.dev.storeapp.databinding.FragmentAddProductDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddProductDialogFragment : BaseDialogFragment<FragmentAddProductDialogBinding>() {

    private val viewModel :ProductsViewModel by viewModels()
    companion object {
        const val TAG = "AddProductDialogFragment"
        fun newInstance() = AddProductDialogFragment()
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddProductDialogBinding {
        return FragmentAddProductDialogBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.imgClose.setOnClickListener {
            dismiss()
        }
        binding.btnSave.setOnClickListener{
            binding.apply {
                val entity = ProductEntity()
                if(edtTitle.text.isEmpty() || edtPrice.text.isEmpty() ||edtCategory.text.isEmpty() || edtBrand.text.isEmpty() || edtDiscount.text.isEmpty() || edtColor.text.isEmpty() ||edtDescription.text.isEmpty() || edtImage.text.isEmpty()){
                    Toast.makeText(requireContext(), "Please Fill All Fileds", Toast.LENGTH_SHORT).show()
                }else{
                    entity.apply {
                        title = edtTitle.text.toString()
                        price = edtPrice.text.toString().toDouble()
                        brand =edtBrand.text.toString()
                        category=edtCategory.text.toString()
                        color = edtColor.text.toString()
                        discount = edtDiscount.text.toString().toInt()
                        description = edtDescription.text.toString()
                        model = edtModel.text.toString()
                        image = edtImage.text.toString()
                    }
                    viewModel.insertProduct(entity)
                    viewModel.createProductToServer(entity)
                    Toast.makeText(requireContext(), "product added Successfully..!", Toast.LENGTH_SHORT).show()
                    dismiss()
                    clearFocus()
                }
            }
        }
    }
    private fun clearFocus(){
        binding.apply {
            edtTitle.setText("")
            edtPrice.setText("")
            edtBrand.setText("")
            edtCategory.setText("")
            edtColor.setText("")
            edtDiscount.setText("")
            edtDescription.setText("")
            edtModel.setText("")
            edtImage.setText("")
        }
    }

}