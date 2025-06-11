package com.dev.storeapp.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.storeapp.R
import com.dev.storeapp.app.base.BaseFragment
import com.dev.storeapp.databinding.FragmentLoginBinding
import com.dev.storeapp.presentation.ui.Home.HomeFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "LoginFragment"
        fun newInstance() = LoginFragment()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        initViews()
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

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
            }

            signIn(email, password)
        }

        binding.signupText.setOnClickListener {
            try{
                replaceFragment(R.id.fragment_container_view, SignUpFragment.newInstance(), false)
            }catch (e: Exception) {
                Log.e(TAG, "Error navigating to SignUpFragment: ${e.localizedMessage}")
                Toast.makeText(requireContext(), "Error navigating to Sign Up", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload() // Implement your reload logic
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.localizedMessage ?: "Invalid Credentials"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Implement your UI updates based on the user's sign-in status
        if (user != null) {
            try{
            replaceFragment(R.id.fragment_container_view, HomeFragment.newInstance(), false)
        }catch (e: Exception) {
            Log.e(TAG, "Error navigating to HomeFragment: ${e.localizedMessage}")
            Toast.makeText(requireContext(), "Error navigating to Home", Toast.LENGTH_SHORT).show()
        }
        } else {
            // Handle the case where the user is not signed in
            Log.d(TAG, "User is not signed in")
        }
    }

    private fun reload() {
        // Implement your logic to reload or navigate after successful sign-in
        // Navigate to home fragment if user is already logged in
//        replaceFragment(R.id.fragment_container, HomeFragment.newInstance(), false)
    }
}