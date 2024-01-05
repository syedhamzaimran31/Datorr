package com.example.taskclass.Actvities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskclass.databinding.ActivitySummaryBinding
import com.example.taskclass.room.Advance
import com.example.taskclass.room.AppDatabase
import com.example.taskclass.room.Basic
import com.example.taskclass.room.DatabaseBuilder
import com.example.taskclass.room.FemaleActivityData
import com.example.taskclass.room.MaleActivityData
import com.example.taskclass.room.Pro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class summaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySummaryBinding
    private lateinit var database: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseBuilder.getInstance(applicationContext)

        val textViewMale: TextView = binding.maleFormCount
        val textViewFemale: TextView = binding.femaleFormCount
        val textViewAdvance: TextView = binding.advanceFormCount

        val textViewBasic: TextView = binding.basicCount
        val textViewAdvance_2: TextView = binding.advanceCount
        val textViewPro: TextView = binding.proCount

        CoroutineScope(Dispatchers.IO).launch {

            val userListMaleActivity: List<MaleActivityData> = database.userDao().getAllMales()
            val userListFemaleActivity: List<FemaleActivityData> =
                database.userDao().getAllFemales()

            val userListBasic: List<Basic> = database.userDao().getAllBasic()
            val userListAdvance: List<Advance> = database.userDao().getAllAdvance()
            val userListPro: List<Pro> = database.userDao().getAllPro()

            for (male in userListMaleActivity) {

                var userIdMale = male.userID ?: 0

                withContext(Dispatchers.Main) {
                    if (userIdMale != null) {
                        textViewMale.text = userIdMale.toString()

                    } else {
                        userIdMale = 0
                        textViewMale.text = userIdMale.toString()
                    }
                }
            }
            for (female in userListFemaleActivity) {

                var userIdFemale = female.userID ?: 0
                Log.d("TAG","$userIdFemale This is female userID" )

                withContext(Dispatchers.Main) {
                        textViewFemale.text = userIdFemale.toString()
                }
            }
            for (basic in userListBasic) {
                val userIdBasic = basic.userId ?: 0
                withContext(Dispatchers.Main) {
                    textViewBasic.text = userIdBasic.toString()
                }
            }
            for (advance in userListAdvance) {
                val userIdAdvance = advance.userId ?: 0
                withContext(Dispatchers.Main) {
                    textViewAdvance_2.text = userIdAdvance.toString()
                }
            }
            for (pro in userListPro) {
                val userIdPro = pro.userId ?: 0
                withContext(Dispatchers.Main) {
                    textViewPro.text = userIdPro.toString()
                }
            }


        }

    }
}