package com.example.taskclass.Actvities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskclass.Adapters.CustomAdapter
import com.example.taskclass.models.ItemsViewModel
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityOptionBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..2) {

            val genderImage = if (i % 2 == 0) R.drawable.male_ else R.drawable.female__
            val genderText = if (i % 2 == 0) "Male" else "Female"
            data.add(ItemsViewModel(genderImage,genderText));
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data);

        // Setting the Adapter with the recyclerview
        binding.recyclerview.adapter = adapter;

        binding.signOutBtn.setOnClickListener {

            Firebase.auth.signOut();
            Toast.makeText(applicationContext,"Logged Out", Toast.LENGTH_SHORT).show();

            val intent = Intent(applicationContext, loginActivity::class.java);
            startActivity(intent);
            finish();

        }
    }
}

