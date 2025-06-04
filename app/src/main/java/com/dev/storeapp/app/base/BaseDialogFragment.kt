package com.dev.storeapp.app.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.dev.storeapp.R


abstract class BaseDialogFragment<VB :ViewBinding> : DialogFragment() {

    private var _binding :VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = getBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    abstract fun getBinding(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?):VB
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}