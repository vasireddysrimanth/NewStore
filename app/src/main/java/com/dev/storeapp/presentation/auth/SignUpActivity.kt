package com.dev.storeapp.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.storeapp.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initViews()
    }

    private fun initViews() {
        binding.singUpButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim() // Trim whitespace
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
            } else if (password.length < 6) {
                binding.passwordInput.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            createAccount(email, password)
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    sendEmailVerification()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.localizedMessage ?: "Signup failed."
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification email sent.", Toast.LENGTH_SHORT,).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Implement your UI updates after successful signup
        if (user != null) {
            Toast.makeText(baseContext, "Signup successful! Verification email sent.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
             finish()
        }
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}