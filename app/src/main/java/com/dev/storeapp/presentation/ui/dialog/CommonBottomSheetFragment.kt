package com.dev.storeapp.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.databinding.FragmentCommonBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CommonBottomSheetFragment : BottomSheetDialogFragment() {

    companion object{
        const val TAG = "CommonBottomSheetFragment"

        fun newInstance(headerMessage: String,lowerMessage :String? = null): CommonBottomSheetFragment {
            val args = Bundle()
            args.putString("message",headerMessage)
            args.putString("lowerMessage",lowerMessage)
            val fragment = CommonBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentCommonBottomSheetBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding  = FragmentCommonBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }
        arguments?.let {bundle ->
            bundle.getString("message")?.let { message ->
                binding.textMessage.text = message }
            bundle.getString("lowerMessage")?.let { lm->
                binding.textSecondMessage.text = lm
            }
        }
        (dialog as? BottomSheetDialog)?.behavior?.state = STATE_EXPANDED
    }

}