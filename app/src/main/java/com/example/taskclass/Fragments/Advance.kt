package com.example.taskclass.Fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taskclass.R
import com.example.taskclass.databinding.AdvanceBinding

class Advance : Fragment() {

    private lateinit var binding: AdvanceBinding
    private var yearPassing_spinner: String? = null

    private val REQUEST_CODE = 50
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private var documentUpload_1: ImageView? = null
    private var documentUpload_2: ImageView? = null
    private var documentUpload_3: ImageView? = null
    private var documentUpload_4: ImageView? = null

    companion object {
        private const val REQUEST_APN_PERMISSION = 123
        private const val picId_1 = 11
        private const val picId_2 = 22
        private const val picId_3 = 33
        private const val picId_4 = 44
        private const val REQUEST_CODE = 70
        private const val REQUEST_CODE_PDF = 71

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = AdvanceBinding.inflate(inflater, container, false)

        documentUpload_1 = binding.document1
        documentUpload_2 = binding.document2
        documentUpload_3 = binding.document3
        documentUpload_4 = binding.document4

        val TextViewAdvance = binding.tvAdvance

        tvUnderline(TextViewAdvance)
        uploadDocument();
        chooseMedia();

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner()
        documentUpload_1?.setOnClickListener {
            checkCameraPermissionAndOpenCamera(picId_1)
        }
        documentUpload_2?.setOnClickListener {
            checkCameraPermissionAndOpenCamera(picId_2)
        }
        binding.submitBtn.setOnClickListener {
            Basic.checkFormSubmit=true;
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PDF && resultCode == Activity.RESULT_OK) {

            val selectedPdfUri = data?.data

            val pdfFileName = getFileName(selectedPdfUri)

            showPdfPreview(pdfFileName)
        }

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val selectedPdfUri = data?.data

            val pdfFileName = getFileName(selectedPdfUri)

            showPdfPreview(pdfFileName)
        }

        if (requestCode == picId_1 && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            val documetUpload_1: ImageView? = view?.findViewById(R.id.document_1)

            documetUpload_1?.setImageBitmap(imageBitmap)

        }
        if (requestCode == picId_2 && resultCode == Activity.RESULT_OK) {
            // Get the captured image from the intent
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            // Assuming you have an ImageView in your layout with the ID cnicBackImageView
            val documenUpload_2: ImageView? = view?.findViewById(R.id.document_2)

            // Set the captured image on your ImageView
            documenUpload_2?.setImageBitmap(imageBitmap)

            // Optionally, you can save the image to a file or handle it further
        }
    if (requestCode == picId_3 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val photo = BitmapFactory.decodeStream(inputStream)
                    documentUpload_3?.setImageBitmap(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Handle case where data is null
            }
        } else if (requestCode == picId_4 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val photo = BitmapFactory.decodeStream(inputStream)
                    documentUpload_4?.setImageBitmap(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Handle case where data is null
            }
        }
    }

    private fun spinner() {
        val yearPassingSpinner = resources.getStringArray(R.array.yearPassingSpinner)
        val spinner = binding.yearSpinner
        val adapter = ArrayAdapter(
            requireContext(), R.layout.custom_spinner_dropdown_item, yearPassingSpinner
        )

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                yearPassing_spinner = yearPassingSpinner[position].toString()
                if (position != 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " + "" + yearPassingSpinner[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle when nothing is selected
            }
        }
    }

    private fun uploadDocument() {

        documentUpload_3?.setOnClickListener(View.OnClickListener { _: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_3)
        })
        documentUpload_4?.setOnClickListener(View.OnClickListener { _: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_4)
        })
    }

    private fun chooseMedia() {
        binding.uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, REQUEST_CODE)
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
        val imageViewPdfPreview: ImageView? = view?.findViewById(R.id.imageviewAdvance_pdf)

        // Set a placeholder image or PDF icon as the preview
        imageViewPdfPreview?.setImageResource(R.drawable.pdf)
        imageViewPdfPreview?.visibility = View.VISIBLE;
        // Optionally, you can also display the PDF file name in a Toast
        Toast.makeText(context, "Selected PDF: $pdfFileName", Toast.LENGTH_SHORT).show()
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
                openCamera(picId_1)
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
    private fun tvUnderline(TextViewAdvance: TextView) {
        val mString = "Advance Form"

        // Creating a Spannable String
        // from the above string
        val mSpannableString = SpannableString(mString)

        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)

        TextViewAdvance.text = mSpannableString
    }
}

