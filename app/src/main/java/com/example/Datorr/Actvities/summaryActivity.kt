package com.example.Datorr.Actvities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Datorr.databinding.ActivitySummaryBinding
import com.example.Datorr.room.Advance
import com.example.Datorr.room.AppDatabase
import com.example.Datorr.room.Basic
import com.example.Datorr.room.DatabaseBuilder
import com.example.Datorr.room.FemaleActivityData
import com.example.Datorr.room.MaleActivityData
import com.example.Datorr.room.Pro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.List
import kotlin.collections.List as List1


class summaryActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences

    private lateinit var binding: ActivitySummaryBinding
    private lateinit var database: AppDatabase
    private var email_sh: String? = null
    private var password_sh: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseBuilder.getInstance(applicationContext)
        sharedpreferences = getSharedPreferences(loginActivity.SHARED_PREFS, Context.MODE_PRIVATE)

        val textViewEmail: TextView = binding.emailUser
        val textViewPassword: TextView = binding.passUser

        val textViewMale: TextView = binding.maleFormCount
        val textViewFemale: TextView = binding.femaleFormCount
        val textViewAdvance: TextView = binding.advanceFormCount

        val textViewBasic: TextView = binding.basicCount
        val textViewAdvance_2: TextView = binding.advanceCount
        val textViewPro: TextView = binding.proCount

        email_sh = sharedpreferences.getString(loginActivity.EMAIL_KEY, null)
        password_sh = sharedpreferences.getString(loginActivity.PASSWORD_KEY, null)

        CoroutineScope(Dispatchers.IO).launch {

            val userListMaleActivity: List1<MaleActivityData> = database.userDao().getAllMales()
            val userListFemaleActivity: List1<FemaleActivityData> =
                database.userDao().getAllFemales()

            val userListBasic: List1<Basic> = database.userDao().getAllBasic()
            val userListAdvance: List1<Advance> = database.userDao().getAllAdvance()
            val userListPro: List1<Pro> = database.userDao().getAllPro()

            textViewEmail.text = email_sh
            textViewPassword.text = password_sh

            val userIdAdvanceValue = userIdAdvanceFunc(userListBasic, userListAdvance, userListPro)
            Log.d("TAG", "userIdAdvance: $userIdAdvanceValue ")
            textViewAdvance.text = userIdAdvanceValue.toString()

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
                Log.d("TAG", "$userIdFemale This is female userID")

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

    private suspend fun userIdAdvanceFunc(
        userListBasic: List<Basic>,
        userListAdvance: List<Advance>,
        userListPro: List<Pro>
    ): Int {
        var userIdBasic = 0
        var userIdAdvance = 0
        var userIdPro = 0

        // Calculate sum for Basic entities
        for (basic in userListBasic) {
            userIdBasic = basic.userId?.toInt() ?: 0
        }

        // Calculate sum for Advance entities
        for (advance in userListAdvance) {
            userIdAdvance = advance.userId?.toInt() ?: 0
        }

        // Calculate sum for Pro entities
        for (pro in userListPro) {
            userIdPro = pro.userId?.toInt() ?: 0
        }

        Log.d("TAG", "userIdBasic: $userIdBasic")
        Log.d("TAG", "userIdAdvance: $userIdAdvance")
        Log.d("TAG", "userIdPro: $userIdPro")

        // Display toasts on the main thread
        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, "userIdBasic: $userIdBasic", Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, "userIdAdvance: $userIdAdvance", Toast.LENGTH_SHORT).show()
            Toast.makeText(applicationContext, "userIdPro: $userIdPro", Toast.LENGTH_SHORT).show()
        }


        return (userIdBasic + userIdAdvance + userIdPro)
    }
}