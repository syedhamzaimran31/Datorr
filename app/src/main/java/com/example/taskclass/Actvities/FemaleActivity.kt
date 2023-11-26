package com.example.taskclass.Actvities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityDashBoardBinding
import com.example.taskclass.databinding.ActivityFemaleBinding
import com.example.taskclass.room.AppDatabase
import com.example.taskclass.room.DatabaseBuilder
import com.example.taskclass.room.FemaleActivityData
import com.example.taskclass.room.MaleActivityData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Calendar

class FemaleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFemaleBinding
    private lateinit var database: AppDatabase
    lateinit var pickDateBtn: Button
    lateinit var selectedDateTV: TextView
    lateinit var clickImageId:CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFemaleBinding.inflate(layoutInflater);
        setContentView(binding.root);

        database = DatabaseBuilder.getInstance(this)

        clickImageId = binding.profileImg;
        pickDateBtn=binding.birthDateBtn;
        selectedDateTV=binding.dateTv;

        clickImageId.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, FemaleActivity.pic_id)
        })

        binding.submitBtn.setOnClickListener {

            val name = binding.nameEt.text.toString();
            val lastName = binding.LastNameEt.text.toString();
            val email = binding.emailEt.text.toString();
            val password = binding.passEt.text.toString();


            if (android.text.TextUtils.isEmpty(binding.nameEt.text.toString().trim())) {
                binding.nameEt.setError("Name cannot be empty")
                return@setOnClickListener
            }
            if (android.text.TextUtils.isEmpty(binding.LastNameEt.text.toString().trim())) {
                binding.LastNameEt.setError("Phone number can not be empty")
                return@setOnClickListener

            }
            if (android.text.TextUtils.isEmpty(binding.emailEt.text.toString().trim())) {
                binding.emailEt.setError("Email cannot be empty")
                return@setOnClickListener
            }
            if (android.text.TextUtils.isEmpty(binding.passEt.text.toString().trim())) {
                binding.passEt.setError("Password cannot be empty")
                return@setOnClickListener
            } else {
                val femaleData = FemaleActivityData(
                    firstName = name,
                    lastName = lastName,
                    email = email,
                    password = password
                )

                database.UserDao().insertFemaleData(femaleData)


            }


        }

        val languages = resources.getStringArray(R.array.Languages)

        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@FemaleActivity,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        pickDateBtn.setOnClickListener {

            val c = Calendar.getInstance()


            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                    selectedDateTV.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },

                year,
                month,
                day
            )

            datePickerDialog.show()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                try {
                    val photo = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    clickImageId.setImageBitmap(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("MaleActivity", "Error loading image from gallery")
                }
            } else {
                Log.e("MaleActivity", "No data or extras found in the result")
            }
        }
    }

    companion object {
        // Define the pic id
        private const val pic_id = 123
    }
}