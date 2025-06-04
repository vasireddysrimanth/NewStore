package com.dev.storeapp.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dev.storeapp.MainActivity
import com.dev.storeapp.R
import com.dev.storeapp.databinding.ActivityLoginBinding
import com.dev.storeapp.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater) // Inflate the layout using View Binding
        setContentView(binding.root) // Set the content view to the root of the binding

        // Initialize Firebase Auth
        auth = Firebase.auth
        initViews()
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if(email.isEmpty()){
                Toast.makeText(baseContext, "Please enter email .", Toast.LENGTH_SHORT).show()
            }else if(password.isEmpty()){
                Toast.makeText(baseContext, "Please enter password.", Toast.LENGTH_SHORT).show()
            }else if (email.isNotEmpty() && password.isNotEmpty()){
                signIn(email, password)
            }

        }
        binding.signupText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
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
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(baseContext, "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Invalid Credentials ", Toast.LENGTH_SHORT,).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Implement your UI updates based on the user's sign-in status
        if (user != null) {
             startActivity(Intent(this, MainActivity::class.java))
             finish()
        }
    }

    private fun reload() {
        // Implement your logic to reload or navigate after successful sign-in
        // For example, navigate to the main activity
        // startActivity(Intent(this, MainActivity::class.java))
        // finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}