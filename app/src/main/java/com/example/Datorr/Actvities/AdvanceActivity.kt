package com.example.Datorr.Actvities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Datorr.Adapters.MyPagerAdapter
import com.example.Datorr.databinding.ActivityAdvanceBinding

class AdvanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdvanceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        binding.viewpagerMain.adapter= fragmentAdapter

        binding.tabsMain.setupWithViewPager(binding.viewpagerMain)

    }
}