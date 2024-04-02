package com.example.Datorr.Actvities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Datorr.Adapters.CustomAdapter
import com.example.Datorr.R
import com.example.Datorr.databinding.ActivityOptionBinding
import com.example.Datorr.models.ItemsViewModel
import com.example.Datorr.room.AppDatabase
import com.example.Datorr.room.DatabaseBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class OptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: AppDatabase

    companion object {
            const val SHARED_PREFS = "shared_prefs"
            const val EMAIL_KEY = "email_key"
            const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = DatabaseBuilder.getInstance(this)
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..3) {

            val genderImage = when (i) {
                1 -> {R.drawable.male_}
                2 -> {R.drawable.female__}
                else -> {R.drawable.user}
            }
            val genderText = when (i) {
                1 -> {"Male"}
                2 -> {"Female"}
                else -> {"Advanced "}
            }
            data.add(ItemsViewModel(genderImage, genderText));
        }

        val adapter = CustomAdapter(data, database);

        binding.recyclerview.adapter = adapter;

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logOut -> {
                    val editor = sharedpreferences.edit().clear()
                    editor.apply()
                    val intent = Intent(applicationContext, loginActivity::class.java);
                    startActivity(intent);
                    finishAffinity();
                    true
                }
                R.id.summary -> {
                    val intent= Intent(applicationContext,summaryActivity::class.java)
                    startActivity(intent)
                    finishAffinity();
                    true
                }

                else -> false
            }
        }
//        binding.signOutBtn.setOnClickListener {

//            Firebase.auth.signOut();
//            Toast.makeText(applicationContext,"Logged Out", Toast.LENGTH_SHORT).show();
//
//            val intent = Intent(applicationContext, loginActivity::class.java);
//            startActivity(intent);
//            finish();
//
//        }
    }


}

