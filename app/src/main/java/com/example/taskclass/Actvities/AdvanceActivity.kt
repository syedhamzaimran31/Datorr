package com.example.taskclass.Actvities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityAdvanceBinding
import com.example.taskclass.databinding.ActivityFormBinding

class AdvanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdvanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
}
}