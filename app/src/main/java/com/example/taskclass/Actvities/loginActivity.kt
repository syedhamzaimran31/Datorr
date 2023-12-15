package com.example.taskclass.Actvities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityLoginBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class loginActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email_sh: String? = null
    private var password_sh: String? = null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        if (isFreshInstallation(sharedpreferences)) {
            Log.d("MyApplication", "Fresh installation")

            // Perform actions for a fresh installation, such as resetting shared preferences
            resetSharedPreferences(sharedpreferences)
        }
        auth = Firebase.auth

        email_sh = sharedpreferences.getString(EMAIL_KEY, null)
        password_sh = sharedpreferences.getString(PASSWORD_KEY, null)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEt.editText?.addTextChangedListener {
            binding.emailEt.error = null
        }
        binding.passEt.editText?.addTextChangedListener {
            binding.passEt.error = null
        }

        binding.emailEt.editText?.setText(email_sh)
        binding.passEt.editText?.setText(password_sh)

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

            } else {
                val editor = sharedpreferences.edit()

                editor.putString(EMAIL_KEY, email)
                editor.putString(PASSWORD_KEY, password)
                editor.apply()
                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Log.d("TAG", "signInWithEmail:success");
                            val user = auth.currentUser
                            updateUI(user);

                        } else {

                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "User not found",
                                Toast.LENGTH_SHORT,
                            ).show()
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

        val editor = sharedpreferences.edit()
        editor.putString(EMAIL_KEY, null) // Clear email
        editor.putString(PASSWORD_KEY, null) // Clear password
        editor.apply()

        binding.signUpTv.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent);
        }
    }

    private fun isFreshInstallation(sharedPreferences: SharedPreferences): Boolean {
        return !sharedPreferences.contains("app_installed_before")
    }

    private fun resetSharedPreferences(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putBoolean("app_installed_before", true)
        editor.apply()
    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(applicationContext, "LogIn successful", Toast.LENGTH_SHORT).show();

        val intent = Intent(applicationContext, OptionActivity::class.java);
        startActivity(intent);
    }

    public override fun onStart() {
        super.onStart()


        if (email_sh != null && password_sh != null) {
            Toast.makeText(applicationContext, "Already Logged In", Toast.LENGTH_SHORT).show()
            val i = Intent(this@loginActivity, OptionActivity::class.java)
            startActivity(i)
        }
   }
}