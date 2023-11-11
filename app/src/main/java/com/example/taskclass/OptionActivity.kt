package com.example.taskclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskclass.databinding.ActivityLoginBinding
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
        for (i in 1..20) {
            data.add(ItemsViewModel(R.drawable.female, "Item " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data);

        // Setting the Adapter with the recyclerview
        binding.recyclerview.adapter = adapter;

    }
}

