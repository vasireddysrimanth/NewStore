package com.dev.storeapp.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.data.model.CategoryDestination
import com.dev.storeapp.data.model.MainCategory
import com.dev.storeapp.data.model.TopPicks
import com.dev.storeapp.data.model.TopPicksDestination
import com.dev.storeapp.databinding.FragmentSettingsBinding
import com.dev.storeapp.presentation.adapter.LeftMenuAdapter
import com.dev.storeapp.presentation.adapter.MainCategoryAdapter
import com.dev.storeapp.presentation.ui.order.OrderFragment
import com.dev.storeapp.presentation.ui.products.ProductFragment
import com.dev.storeapp.presentation.ui.you.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsFragment()
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
        setupRecyclerViews()
    }

    private fun setupBottomNavigation() {
        binding.orderButton.setOnClickListener { navigateToFragment(OrderFragment.newInstance()) }
        binding.buyAgain.setOnClickListener { navigateToFragment(ProductFragment.newInstance()) }
        binding.profileButton.setOnClickListener { navigateToFragment(ProfileFragment.newInstance()) }
        binding.ListsButton.setOnClickListener { navigateToFragment(ProfileFragment.newInstance()) }
    }

    private fun setupRecyclerViews() {
        binding.leftMenuRecyclerView.apply {
            adapter = LeftMenuAdapter(getLeftMenuFeatures()) { handleLeftMenuClick(it) }
        }

        binding.mainCategoriesRecyclerView.apply {
            adapter = MainCategoryAdapter { handleMainCategoryClick(it) }.apply {
                submitList(getMainCategories())
            }
        }
    }

    private fun getLeftMenuFeatures() = listOf(
        TopPicksDestination("Mobiles &\nElectronics", TopPicks.MOBILES_ELECTRONICS, R.drawable.ic_mobiles),
        TopPicksDestination("Audio & Video", TopPicks.AUDIO_VIDEO, R.drawable.ic_audio),
        TopPicksDestination("Televisions", TopPicks.TELEVISIONS, R.drawable.ic_tv),
        TopPicksDestination("Account & Help", TopPicks.ACCOUNT_HELP, R.drawable.ic_help)
    )

    private fun getMainCategories() = listOf(
        MainCategory("Mobiles", CategoryDestination.MOBILES, R.drawable.ic_mobiles),
        MainCategory("Video & Music", CategoryDestination.VIDEO_MUSIC, R.drawable.ic_audio),
        MainCategory("Electronics & TV", CategoryDestination.ELECTRONICS_TV, R.drawable.ic_tv),
        MainCategory("Gaming", CategoryDestination.GAMING, R.drawable.ic_gaming),
        MainCategory("Home", CategoryDestination.HOME, R.drawable.ic_home),
        MainCategory("Grocery", CategoryDestination.GROCERY, R.drawable.ic_grocery),
        MainCategory("Kids & Toys", CategoryDestination.KIDS_TOYS, R.drawable.ic_kidstoys)
    )

    private fun handleLeftMenuClick(destination: TopPicksDestination) {
        val fragment = when (destination.destination) {
            TopPicks.MOBILES_ELECTRONICS -> ProductFragment.newInstance("mobile")
            TopPicks.AUDIO_VIDEO -> ProductFragment.newInstance("audio")
            TopPicks.TELEVISIONS -> ProductFragment.newInstance("tv")
            TopPicks.ACCOUNT_HELP -> ProfileFragment.newInstance()
        }
        navigateToFragment(fragment)
    }

    private fun handleMainCategoryClick(destination: CategoryDestination) {
        val fragment = when (destination) {
            CategoryDestination.MOBILES -> ProductFragment.newInstance("mobile")
            CategoryDestination.VIDEO_MUSIC -> ProductFragment.newInstance("audio")
            CategoryDestination.ELECTRONICS_TV -> ProductFragment.newInstance("tv")
            CategoryDestination.GAMING -> ProductFragment.newInstance("gaming")
            CategoryDestination.HOME -> ProductFragment.newInstance("home")
            CategoryDestination.GROCERY -> ProductFragment.newInstance("grocery")
            CategoryDestination.KIDS_TOYS -> ProductFragment.newInstance("toys")
        }
        navigateToFragment(fragment)
    }

    private fun navigateToFragment(fragment: Fragment) {
        replaceFragment(R.id.fragment_container, fragment, true)
    }
}