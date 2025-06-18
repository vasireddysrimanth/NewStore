package com.dev.storeapp.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.dev.storeapp.app.utils.ProgressDialogUtils
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment <VB: ViewBinding> :Fragment(){
    private var _binding : VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding =getBinding(inflater,container,savedInstanceState)
        return binding.root

    }
    protected abstract fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ):VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //snackBar
    fun showSnackBar(message: String,contextView: View){
        activity.let {
            Snackbar.make(contextView,message,Snackbar.LENGTH_SHORT).show()
        }
    }

    fun replaceFragment(
            containerId :Int,
            newInstance: Fragment,
            addToBackStack: Boolean =false,
            tag: String? = null
    ){
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            replace(containerId,newInstance,tag)
            if(addToBackStack){
                addToBackStack(tag)
            }
        }

    }

    fun replaceFragmentOnly(
        containerId: Int,
        newInstance: Fragment,
        tag: String? = null
    ){
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            replace(containerId,newInstance,tag)
            setReorderingAllowed(true)
        }
    }

    fun goBack() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun clearAllBackStack(){
        activity?.supportFragmentManager?.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


     private val progressDialog by lazy {
         ProgressDialogUtils(requireContext())
     }
    fun showLoader() = progressDialog.showProgress()
    fun hideLoader() = progressDialog.dismissProgress()



}