package com.example.taskclass

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskclass.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBt.setOnClickListener {
            val email = binding.emailEt.editText?.text?.toString();
            val password = binding.passEt.editText?.text?.toString();

            if (TextUtils.isEmpty(binding.emailEt.editText?.text.toString().trim())) {
                binding.emailEt.setError("Name cannot be empty")
            }
            if (TextUtils.isEmpty(binding.passEt.editText?.text.toString().trim())) {
                binding.passEt.setError("Email cannot be empty")
            }

                else {
                Toast.makeText(applicationContext, "Log In Successful", Toast.LENGTH_SHORT)
                    .show();

            }
        }
        binding.signUpTv.setOnClickListener{
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }
    }
}