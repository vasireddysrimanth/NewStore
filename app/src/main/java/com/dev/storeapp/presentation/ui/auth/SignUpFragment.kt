package com.dev.storeapp.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.app.utils.AppLogger
import com.dev.storeapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    companion object {
        private const val TAG = "SignUpFragment"
        fun newInstance() = SignUpFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initViews()
    }

    private fun initViews() {
        binding.singUpButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val gender = binding.genderInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username.isEmpty()) {
                binding.usernameInput.error = "Username is required"
                return@setOnClickListener
            }
            if (gender.isEmpty()) {
                binding.genderInput.error = "Gender is required"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.emailInput.error = "Email is required"
                return@setOnClickListener
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInput.error = "Enter a valid email address"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInput.error = "Password is required"
                return@setOnClickListener
            } else if (password.length < 6) {
                binding.passwordInput.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            createAccount(username, gender, email, password)
        }

        binding.loginLink.setOnClickListener {
            try {
                replaceFragment(R.id.fragment_container_view, LoginFragment.newInstance(), false)
            } catch (e: Exception) {
                Log.e(TAG, "Error replacing fragment: ${e.message}")
                Toast.makeText(requireContext(), "Error navigating to login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAccount(username: String, gender: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    AppLogger.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    saveUserToFirestore(user, username, gender)
                    updateUI(user)
                    sendEmailVerification()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.localizedMessage ?: "Signup failed."
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?, username: String, gender: String) {
        if (user == null) return
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val userMap = hashMapOf(
            "uid" to user.uid,
            "username" to username,
            "email" to user.email,
            "gender" to gender,
            "created_at" to currentDate
        )
        db.collection("users").document(user.uid)
            .set(userMap)
            .addOnSuccessListener {
                AppLogger.d(TAG, "User added to Firestore")
            }
            .addOnFailureListener { e ->
                AppLogger.e(TAG, "Error adding user to Firestore:  $e")
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Verification email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(requireContext(), "Signup successful! Verification email sent.", Toast.LENGTH_LONG).show()
            replaceFragment(R.id.fragment_container_view, LoginFragment.newInstance(), false)
        }
    }
}