package com.dev.storeapp.presentation.ui.dialog

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.dev.storeapp.R
import com.dev.storeapp.app.common.Result
import com.dev.storeapp.app.extensions.launchAndRepeatOnStarted
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.data.local.entity.FireBaseUserEntity
import com.dev.storeapp.databinding.FragmentProfileEditBottomSheetBinding
import com.dev.storeapp.presentation.ui.you.UserDetailsFragment
import com.dev.storeapp.presentation.ui.you.UsersViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@AndroidEntryPoint
class ProfileEditBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileEditBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var selectedImagePath: String? = null
    private var imageUpdateTimestamp: Long = System.currentTimeMillis()

    companion object {
        fun newInstance() = ProfileEditBottomSheetFragment()
        const val TAG = "ProfileEditBottomSheetFragment"
    }

    private val viewModel: UsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        createObserver()
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = requireContext().contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            }
            bitmap?.let {
                val userId = binding.editTextUid.text.toString()
                val path = saveImageToInternalStorage(it, userId)
                selectedImagePath = path
                imageUpdateTimestamp = System.currentTimeMillis()

                // Clear Glide cache for this specific image
                clearImageCache(path)

                // Show image instantly with updated cache signature
                loadProfileImage(path)
            }
        }
    }

    private fun createObserver() {
        launchAndRepeatOnStarted {
            viewModel.userUiState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        AppLogger.d(UserDetailsFragment.TAG, "Loading user details...")
                    }
                    is Result.Success -> {
                        updateUI(result.data)
                        AppLogger.d(UserDetailsFragment.TAG, "Success loading user details.")
                    }
                    is Result.Error -> {
                        AppLogger.e(UserDetailsFragment.TAG, "Error loading user details: ${result.exception?.message}")
                    }
                }
            }
        }
    }

    private fun updateUI(user: FireBaseUserEntity?) = user?.let {
        binding.editTextUid.text = it.uid
        binding.editTextUserName.setText(it.username)
        binding.editTextEmail.setText(it.email)
        binding.editTextGender.setText(it.gender)

        // Show image if exists
        it.profileImagePath?.takeIf { it.isNotBlank() }?.let { path ->
            selectedImagePath = path
            imageUpdateTimestamp = System.currentTimeMillis()
            loadProfileImage(path)
        }
    } ?: AppLogger.e(UserDetailsFragment.TAG, "User data is null!")

    private fun initViews() {
        binding.textChangeProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.imageProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.clearButton.setOnClickListener {
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.buttonContinue.setOnClickListener {
            val updatedUser = FireBaseUserEntity(
                uid = binding.editTextUid.text.toString(),
                username = binding.editTextUserName.text.toString(),
                email = binding.editTextEmail.text.toString(),
                gender = binding.editTextGender.text.toString(),
                createdAt = Timestamp.now(),
                profileImagePath = selectedImagePath
            )
            viewModel.upsertUser(updatedUser)
            dismiss()
        }
    }

    /**
     * Clears Glide cache for the specific image to ensure fresh loading
     */
    private fun clearImageCache(imagePath: String) {
        val file = File(imagePath)
        // Clear memory cache
        Glide.with(requireContext()).clear(binding.imageProfile)

        // Clear disk cache on background thread
        Thread {
            try {
                Glide.get(requireContext()).clearDiskCache()
            } catch (e: Exception) {
                AppLogger.e(TAG, "Error clearing disk cache: ${e.message}")
            }
        }.start()
    }

    /**
     * Loads the profile image using Glide with aggressive cache invalidation
     */
    private fun loadProfileImage(path: String) {
        val file = File(path)
        if (!file.exists()) {
            AppLogger.e(TAG, "Image file does not exist: $path")
            return
        }

        // Create unique signature combining file timestamp and our update timestamp
        val signature = "${file.lastModified()}_${imageUpdateTimestamp}_${UUID.randomUUID()}"

        Glide.with(binding.imageProfile.context)
            .load(file)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache for profile images
            .skipMemoryCache(true) // Skip memory cache to always load fresh
            .signature(ObjectKey(signature)) // Unique signature for cache busting
            .into(binding.imageProfile)
    }

    private fun saveImageToInternalStorage(bitmap: android.graphics.Bitmap, userId: String): String {
        // Create file with timestamp to ensure uniqueness
        val timestamp = System.currentTimeMillis()
        val file = File(requireContext().filesDir, "profile_${userId}_${timestamp}.jpg")

        // Delete old profile images for this user
        cleanupOldProfileImages(userId)

        FileOutputStream(file).use { out ->
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }

    /**
     * Removes old profile images to prevent storage buildup
     */
    private fun cleanupOldProfileImages(userId: String) {
        try {
            val filesDir = requireContext().filesDir
            val oldFiles = filesDir.listFiles { file ->
                file.name.startsWith("profile_${userId}_") && file.name.endsWith(".jpg")
            }
            oldFiles?.forEach { file ->
                file.delete()
                AppLogger.d(TAG, "Deleted old profile image: ${file.name}")
            }
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error cleaning up old profile images: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}