package com.dev.storeapp.presentation.ui.you

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.constants.Constants.SECTION_ABOUT
import com.dev.storeapp.app.constants.Constants.SECTION_NOTIFICATIONS
import com.dev.storeapp.app.constants.Constants.SECTION_PERMISSIONS
import com.dev.storeapp.app.constants.Constants.SECTION_PURCHASE
import com.dev.storeapp.databinding.FragmentSettingsDetailBinding

class SettingsDetailFragment : BaseFragment<FragmentSettingsDetailBinding>() {

    companion object {
        const val TAG = "SettingsDetailFragment"

        fun newInstance(section: String): SettingsDetailFragment {
            val fragment = SettingsDetailFragment()
            val args = Bundle().apply { putString("section", section) }
            fragment.arguments = args
            return fragment
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentSettingsDetailBinding {
        return FragmentSettingsDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displaySection()
    }

    /**
     * Display the selected section and update the related content.
     */
    private fun displaySection() {
        val sectionToShow = arguments?.getString("section")
        val sections = listOf(
            binding.notificationSection,
            binding.permissionSection,
            binding.defaultPurchaseSection,
            binding.aboutSection
        )
        // Hide all sections first
        sections.forEach { it.visibility = View.GONE }

        // Show the selected section and update its content
        when (sectionToShow) {
            SECTION_NOTIFICATIONS -> {
                binding.notificationSection.visibility = View.VISIBLE
                binding.notificationText.text =
                    getString(R.string.please_enable_notifications_to_stay_up_to_date)
            }
            SECTION_PERMISSIONS -> {
                binding.permissionSection.visibility = View.VISIBLE
                binding.permissionText.text =
                    getString(R.string.please_enable_necessary_permissions_e_g_storage_notifications_for_full_functionality)
            }
            SECTION_PURCHASE -> {
                binding.defaultPurchaseSection.visibility = View.VISIBLE
                binding.defaultPurchaseText.text =
                    getString(R.string.currently_there_are_no_default_purchase_preferences_configured)            }
            SECTION_ABOUT -> {
                binding.aboutSection.visibility = View.VISIBLE
                binding.aboutVersion.text = getAppVersionText()
            }
        }
    }

    private fun getAppVersionText(): String {
        val context = activity ?: return ""
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "App Version: ${packageInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            "Version info unavailable"
        }
    }

}