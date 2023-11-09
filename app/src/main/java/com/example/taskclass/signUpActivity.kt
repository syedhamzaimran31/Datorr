package com.example.taskclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.example.taskclass.databinding.ActivitySignUpBinding

class signUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignUpBtn.setOnClickListener {
            val name=binding.nameEt.editText?.text?.toString();
            val pass=binding.passEt.editText?.text?.toString();
            val email=binding.emailEt.editText?.text?.toString();
            val phoneNum=binding.numberEt.editText?.text?.toString();

                if (TextUtils.isEmpty(binding.nameEt.editText?.text.toString().trim())) {
                    binding.nameEt.setError("Name cannot be empty")
                }
                if (TextUtils.isEmpty(binding.emailEt.editText?.text.toString().trim())) {
                    binding.emailEt.setError("Email cannot be empty")
                }
                if (TextUtils.isEmpty(binding.passEt.editText?.text.toString().trim())) {
                    binding.passEt.setError("Password cannot be empty")
                }
                if (TextUtils.isEmpty(binding.numberEt.editText?.text.toString().trim())){
                    binding.numberEt.setError("Phone number can not be empty")
            }else{
                Toast.makeText(applicationContext,"SignUp successful",Toast.LENGTH_SHORT).show();

                val i=Intent(applicationContext,loginActivity::class.java);
                startActivity(i);
            }


        }
            binding.signUpTv.setOnClickListener{
                val i=Intent(applicationContext,loginActivity::class.java);
                startActivity(i);
            }




    }

//    fun CheckAllFields(): Boolean {
//        if (na) {
//            name!!.error = "This field is required"
//            return false
//        }
//        if (pass!!.length() == 0) {
//            pass!!.error = "This field is required"
//            return false
//        }
//        if (email!!.length() == 0) {
//            email!!.error = "Email is required"
//            return false
//        }
//        if (phoneNum!!.length() == 0) {
//            phoneNum!!.error = "Password is required"
//            return false
//        }
//        return true
//    }


}