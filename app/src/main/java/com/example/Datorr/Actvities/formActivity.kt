package com.example.Datorr.Actvities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Datorr.databinding.ActivityFormBinding

class formActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}