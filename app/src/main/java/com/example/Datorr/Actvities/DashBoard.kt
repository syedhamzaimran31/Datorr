package com.example.Datorr.Actvities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.Datorr.databinding.ActivityDashBoardBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class DashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityDashBoardBinding.inflate(layoutInflater);
        setContentView(binding.root);

        binding.signOutBtn.setOnClickListener {

            Firebase.auth.signOut();
            Toast.makeText(applicationContext,"Logged Out", Toast.LENGTH_SHORT).show();

            val intent = Intent(applicationContext, loginActivity::class.java);
            startActivity(intent);
            finish();

        }

    }
}