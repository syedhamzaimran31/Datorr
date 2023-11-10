package com.example.taskclass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.taskclass.databinding.ActivitySignUpBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce


class signUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
                val doubleBounce: Sprite = DoubleBounce()
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateDrawable = doubleBounce

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
}