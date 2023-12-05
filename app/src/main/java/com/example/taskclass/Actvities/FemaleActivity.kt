package com.example.taskclass.Actvities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.taskclass.R
import com.example.taskclass.databinding.ActivityFemaleBinding
import com.example.taskclass.room.AppDatabase
import com.example.taskclass.room.DatabaseBuilder
import com.example.taskclass.room.FemaleActivityData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.util.Calendar

class FemaleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFemaleBinding
    private lateinit var database: AppDatabase
    lateinit var pickDateBtn: Button
    private var selected_Age: String? = null
    private var date_Room: String? = null
    private var id: Int? = null
    private var location_room: String? = null
    private var photoRoom: String? = null
    lateinit var selectedDateTV: TextView
    lateinit var clickImageId: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFemaleBinding.inflate(layoutInflater);
        setContentView(binding.root);

        database = DatabaseBuilder.getInstance(this)

        clickImageId = binding.profileImg;
        pickDateBtn = binding.birthDateBtn;
        selectedDateTV = binding.dateTv;

        clickImageId.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, FemaleActivity.pic_id)
        })


        binding.ageRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            selected_Age = radio.text.toString();
            Toast.makeText(
                applicationContext, " On checked change :" + selected_Age, Toast.LENGTH_SHORT
            ).show()
        })
        // Get radio group selected status and text using button click event
        binding.submitBtn.setOnClickListener {
            // Get the checked radio button id from radio group

            id = binding.ageRadioGroup.checkedRadioButtonId;
            if (id != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(id!!)
                selected_Age = radio.text.toString();
                Toast.makeText(
                    applicationContext, "On button click :" + selected_Age, Toast.LENGTH_SHORT
                ).show()
            } else {
                // If no radio button checked in this radio group
                Toast.makeText(
                    applicationContext,
                    "On button click :" + " nothing selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun radio_button_click(view: View) {
            val radio: RadioButton = findViewById(binding.ageRadioGroup.checkedRadioButtonId)
            selected_Age = radio.text.toString();
            Toast.makeText(
                applicationContext, "On click : ${selected_Age}", Toast.LENGTH_SHORT
            ).show()
        }




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
            }
            if (id == -1) {
                Toast.makeText(
                    applicationContext,
                    "Radio Field is empty",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val femaleData = FemaleActivityData(
                    firstName = name,
                    lastName = lastName,
                    email = email,
                    password = password,
                    isAbove_18 = selected_Age,
                    ageBirth = date_Room,
                    location_room = location_room,
                    photoRoom = photoRoom

                )
                lifecycleScope.launch {
                    database.UserDao().insertFemaleData(femaleData)

                    val i = Intent(applicationContext, OptionActivity::class.java);
                    startActivity(i);
                    finish();
                }
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
                    location_room = languages[position].toString();
                    if (position != 0) {
                        Toast.makeText(
                            this@FemaleActivity,
                            getString(R.string.selected_item) + " " +
                                    "" + languages[position], Toast.LENGTH_SHORT
                        ).show()
                    }
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
                    date_Room = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year);
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
                    photoRoom = imageUri.toString();
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
        private const val pic_id = 123
    }
}