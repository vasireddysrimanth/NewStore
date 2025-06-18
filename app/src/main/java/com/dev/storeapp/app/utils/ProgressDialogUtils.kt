package com.dev.storeapp.app.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.dev.storeapp.databinding.ItemProgressdialogBinding

class ProgressDialogUtils(context: Context) {
    private val dialog = Dialog(context)
    private val view = ItemProgressdialogBinding.inflate(LayoutInflater.from(context))
    init {
        dialog.apply {
            requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(view.root)
        }
    }

    fun showProgress(message: String = "") {
        try {
            if (!dialog.isShowing) {
                dialog.show()
            }
        } catch (_: Exception) {
        }
    }

    fun dismissProgress() {
        try {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        } catch (_: Exception) {
        }
    }

}