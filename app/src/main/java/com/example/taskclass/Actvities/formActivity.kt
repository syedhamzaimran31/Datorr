package com.example.taskclass.Actvities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityFemaleBinding
import com.example.taskclass.databinding.ActivityFormBinding

class formActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}