package com.dev.storeapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.dev.storeapp.databinding.ActivityMainBinding
import com.dev.storeapp.presentation.ui.Home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Post to ensure views are measured
        binding.logoImage.post {
            startAnimation()

            // Wait 3 seconds, then load Home (ProductFragment)
            lifecycleScope.launch {
                delay(5000L)
                binding.logoImage.visibility = View.GONE
                binding.appName.visibility = View.GONE
                binding.bottomText.visibility = View.GONE
                binding.fragmentContainerView.visibility = View.VISIBLE
                supportFragmentManager.commit {
                    replace(R.id.fragment_container_view, HomeFragment.newInstance())
                }
            }
        }
    }

    private fun startAnimation() {
        val centerY = binding.splashContainer.height / 2f - binding.logoImage.height / 2f
        val appNameTargetY = binding.splashContainer.height - binding.appName.height - 100f

        // Start both views from center
        binding.logoImage.y = centerY
        binding.appName.y = centerY

        // Animations
        val appNameMove = ObjectAnimator.ofFloat(binding.appName, "y", appNameTargetY)
        val fadeInLogo = ObjectAnimator.ofFloat(binding.logoImage, "alpha", 0f, 1f)
        val fadeInText = ObjectAnimator.ofFloat(binding.appName, "alpha", 0f, 1f)

        AnimatorSet().apply {
            playTogether(appNameMove, fadeInLogo, fadeInText)
            duration = 1000L
            start()
        }
    }
}
