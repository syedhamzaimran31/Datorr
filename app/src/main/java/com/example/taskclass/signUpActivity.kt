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
import com.example.taskclass.databinding.ActivitySignUpBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.FoldingCube
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class signUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameEt.editText?.addTextChangedListener {
            binding.nameEt.error = null
        }
        binding.numberEt.editText?.addTextChangedListener {
            binding.numberEt.error = null
        }
        binding.emailEt.editText?.addTextChangedListener {
            binding.emailEt.error = null
        }
        binding.passEt.editText?.addTextChangedListener {
            binding.passEt.error = null
        }

        binding.SignUpBtn.setOnClickListener {
            val name = binding.nameEt.editText?.text?.toString();
            val pass = binding.passEt.editText?.text?.toString();
            val email = binding.emailEt.editText?.text?.toString();
            val phoneNum = binding.numberEt.editText?.text?.toString();

            if (TextUtils.isEmpty(binding.nameEt.editText?.text.toString().trim())) {
                binding.nameEt.setError("Name cannot be empty")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.numberEt.editText?.text.toString().trim())) {
                binding.numberEt.setError("Phone number can not be empty")
                return@setOnClickListener

            }
            if (TextUtils.isEmpty(binding.emailEt.editText?.text.toString().trim())) {
                binding.emailEt.setError("Email cannot be empty")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.passEt.editText?.text.toString().trim())) {
                binding.passEt.setError("Password cannot be empty")
                return@setOnClickListener
            }
          else {
                auth.createUserWithEmailAndPassword(email.toString(), pass.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
//                            updateUI(null)
                        }
                    }
                val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
                val wanderingCubes: Sprite = WanderingCubes()
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateDrawable = wanderingCubes

                Toast.makeText(applicationContext, "SignUp successful", Toast.LENGTH_SHORT).show();

                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    val i = Intent(applicationContext, loginActivity::class.java)
                    startActivity(i)
                }, 3000)
            }


        }
        binding.signUpTv.setOnClickListener {
            val i = Intent(applicationContext, loginActivity::class.java);
            startActivity(i);
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(applicationContext,DashBoard::class.java);
        startActivity(intent);
    }
}