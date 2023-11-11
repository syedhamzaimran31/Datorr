package com.example.taskclass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.taskclass.databinding.ActivityLoginBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEt.editText?.addTextChangedListener {
            binding.emailEt.error = null
        }
        binding.passEt.editText?.addTextChangedListener {
            binding.passEt.error = null
        }

        binding.loginBt.setOnClickListener {
            val email = binding.emailEt.editText?.text?.toString();
            val password = binding.passEt.editText?.text?.toString();

            if (TextUtils.isEmpty(binding.emailEt.editText?.text.toString().trim())) {
                binding.emailEt.setError("Email can not be empty")
                return@setOnClickListener

            }
            if (TextUtils.isEmpty(binding.passEt.editText?.text.toString().trim())) {
                binding.passEt.setError("Password can not be empty")
                return@setOnClickListener

            }

                else {

                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            updateUI(null)
                        }
                    }

                val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
                val wanderingCubes: Sprite = WanderingCubes()
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateDrawable = wanderingCubes


                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE

                }, 3000)
            }
        }
        binding.signUpTv.setOnClickListener{
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent);
            Toast.makeText(applicationContext, "LogIn successful", Toast.LENGTH_SHORT).show();
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(applicationContext,DashBoard::class.java);
        startActivity(intent);
    }
}