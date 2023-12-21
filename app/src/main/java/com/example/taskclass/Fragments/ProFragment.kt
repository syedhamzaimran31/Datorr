package com.example.taskclass.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskclass.R
import com.example.taskclass.databinding.ProBinding
import com.example.taskclass.room.DatabaseBuilder
import com.example.taskclass.room.Pro
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


class ProFragment : Fragment() {
    private val REQUEST_CODE_CAMERA = 82
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private var DOB: String? = null
    private var image:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ProBinding = ProBinding.inflate(inflater, container, false)
        var dataBase = DatabaseBuilder.getInstance(requireContext())
        val textViewDOB = binding.textViewDOB
        val timePicker = binding.timePickerDOB
        val TextViewPro = binding.tvPro
        val autoTextView = binding.autoTextView
        val proImage = binding.proImageCardV
        val clearBtn = binding.removeImage


        //Calling Functions
        tvUnderline(TextViewPro)
        autoTextGenerate(autoTextView)
        onClickTime(timePicker, textViewDOB)

        proImage.setOnClickListener {
            checkCameraPermissionAndOpenCamera(REQUEST_CODE_CAMERA)
        }
        clearBtn.setOnClickListener {
            binding.proImage.setImageDrawable(null)
            binding.proImage.setImageResource(R.drawable.add_photo)
            image=null
        }
        autoTextView.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) autoTextView.hint = "" else autoTextView.hint = "type something"
        }
        binding.submit.setOnClickListener {

            if (TextUtils.isEmpty(binding.textViewDOB.text.toString().trim())) {
                binding.textViewDOB.error = "select Time"
                return@setOnClickListener

            }
            if (TextUtils.isEmpty(binding.autoTextView.text.toString().trim())) {
                binding.autoTextView.error = "Auto Text View can not be empty"
                return@setOnClickListener

            }
            if (image == null) {
                Toast.makeText(requireContext(), "Image is required", Toast.LENGTH_SHORT).show()
            } else {
                val autoText = binding.autoTextView.text.toString().trim()
                val Pro = Pro(
                    DOB = DOB,
                    autoText = autoText,
                    image = image!!
                    )
                lifecycleScope.launch {
                    dataBase.userDao().insertPro(Pro)
                }
                Toast.makeText(context, "Data successfully stored", Toast.LENGTH_SHORT).show()
                BasicFragment.checkFormSubmit = true;
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun autoTextGenerate(autoTextView: TextView) {
        if (autoTextView is AutoCompleteTextView) {
            val cities = resources.getStringArray(R.array.Cities)
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
            autoTextView.setAdapter(adapter)
        }

    }

    private fun openCamera(requestCode: Int) {
        // Open the camera using an intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCode)
    }

    private fun checkCameraPermissionAndOpenCamera(requestCode: Int) {
        // Check if the CAMERA permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is granted, open the camera
            openCamera(requestCode)
        } else {
            // If permission is not granted, request the CAMERA permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if the CAMERA permission is granted in the permission result
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, open the camera
                openCamera(REQUEST_CODE_CAMERA)
            } else {
                // If permission is not granted, show a Toast indicating the requirement
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to take pictures.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {

            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap
            val proImageView: ImageView? = view?.findViewById(R.id.proImage)

            proImageView?.setImageBitmap(imageBitmap)
            image=generateUniqueImageName()
        }
    }

    private fun tvUnderline(TextViewPro: TextView) {
        val mString = "Pro Form"

        // Creating a Spannable String
        // from the above string
        val mSpannableString = SpannableString(mString)

        // Setting underline style from
        // position 0 till length of
        // the spannable string
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)

        // Displaying this spannable
        // string in TextView
        TextViewPro.text = mSpannableString
    }

    private fun onClickTime(timePicker: TimePicker, textView: TextView) {


        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = ""
            // AM_PM decider logic
            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }

                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    hour -= 12
                    am_pm = "PM"
                }

                else -> am_pm = "AM"
            }
            if (textView != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "Time is: $hour : $min $am_pm"
                val savingTime="$hour : $min $am_pm"
                textView.text = msg
                DOB = savingTime.toString()
                textView.visibility = ViewGroup.VISIBLE
            }

        }
    }

    fun generateUniqueImageName(): String {
        // Use timestamp to ensure uniqueness
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        // Generate a unique identifier (UUID) to further ensure uniqueness
        val uniqueId = UUID.randomUUID().toString()

        // Combine timestamp and unique identifier to create a unique name
        return "IMG_$timeStamp$uniqueId.jpg"
    }
}