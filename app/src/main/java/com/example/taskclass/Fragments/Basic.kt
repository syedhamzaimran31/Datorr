package com.example.taskclass.Fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taskclass.R
import com.example.taskclass.databinding.BasicBinding


class Basic : Fragment() {

    private val REQUEST_CODE = 50
    private val REQUEST_CODE_CAMERA_1 = 51
    private val REQUEST_CODE_CAMERA_2 = 52
    private val CAMERA_PERMISSION_REQUEST_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BasicBinding = BasicBinding.inflate(inflater, container, false)
        // set variables in Binding


        val TextViewBasic = binding.tvBasic

        tvUnderline(TextViewBasic)


        binding.btnSelectPdf.setOnClickListener {
            Toast.makeText(context, "select a pdf", Toast.LENGTH_SHORT).show()
            chooseMedia()
        }


        binding.cnicFront.setOnClickListener {
            checkCameraPermissionAndOpenCamera(REQUEST_CODE_CAMERA_1)
        }
        binding.cnicBack.setOnClickListener {
            checkCameraPermissionAndOpenCamera(REQUEST_CODE_CAMERA_2)
        }
        binding.btnSubmitBasic.setOnClickListener {
            val name = binding.cnicNumber.text.toString().trim();
            val lastName = binding.phoneNumber.text.toString().trim();
            if (TextUtils.isEmpty(binding.cnicNumber.text.toString().trim())) {
                binding.cnicNumber.setError("CNIC number cannot be empty")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.phoneNumber.text.toString().trim())) {
                binding.phoneNumber.setError("Phone number can not be empty")
                return@setOnClickListener

            } else {
                Toast.makeText(context, "Data successfully stored", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


    //Functions
    private fun chooseMedia() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, REQUEST_CODE)
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

    private fun openCamera(requestCode: Int) {
        // Open the camera using an intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCode)
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
                openCamera(REQUEST_CODE_CAMERA_1)
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

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val selectedPdfUri = data?.data

            val pdfFileName = getFileName(selectedPdfUri)

            showPdfPreview(pdfFileName)
        }
        if (requestCode == REQUEST_CODE_CAMERA_1 && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            val cnicFrontImageView: ImageView? = view?.findViewById(R.id.cnicFront)

            cnicFrontImageView?.setImageBitmap(imageBitmap)

        }
        if (requestCode == REQUEST_CODE_CAMERA_2 && resultCode == Activity.RESULT_OK) {
            // Get the captured image from the intent
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            // Assuming you have an ImageView in your layout with the ID cnicBackImageView
            val cnicBackImageView: ImageView? = view?.findViewById(R.id.cnicBack)

            // Set the captured image on your ImageView
            cnicBackImageView?.setImageBitmap(imageBitmap)

            // Optionally, you can save the image to a file or handle it further
        }
    }

    private fun getFileName(uri: Uri?): String {
        var result = ""
        uri?.let {
            val cursor: Cursor? = activity?.contentResolver?.query(it, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex: Int = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    result = it.getString(nameIndex)
                }
            }
        }
        return result
    }

    private fun showPdfPreview(pdfFileName: String) {
        // Assuming you have an ImageView in your layout with the ID imageViewPdfPreview
        val imageViewPdfPreview: ImageView? = view?.findViewById(R.id.imageviewBasic_pdf)

        // Set a placeholder image or PDF icon as the preview
        imageViewPdfPreview?.setImageResource(R.drawable.pdf)
        imageViewPdfPreview?.visibility = View.VISIBLE;
        // Optionally, you can also display the PDF file name in a Toast
        Toast.makeText(context, "Selected PDF: $pdfFileName", Toast.LENGTH_SHORT).show()
    }

    private fun tvUnderline(TextViewBasic: TextView) {
        val mString = "Basic Form"

        // Creating a Spannable String
        // from the above string
        val mSpannableString = SpannableString(mString)

        // Setting underline style from
        // position 0 till length of
        // the spannable string
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)

        // Displaying this spannable
        // string in TextView
        TextViewBasic.text = mSpannableString
    }
}