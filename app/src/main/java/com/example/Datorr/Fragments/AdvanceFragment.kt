package com.example.Datorr.Fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.Datorr.R
import com.example.Datorr.databinding.AdvanceBinding
import com.example.Datorr.room.Advance
import com.example.Datorr.room.AppDatabase
import com.example.Datorr.room.DatabaseBuilder
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException

class AdvanceFragment : Fragment() {

    private lateinit var binding: AdvanceBinding
    lateinit var dataBase: AppDatabase
    private var yearPassing_spinner: String? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private var documentUpload_1: ImageView? = null
    private var documentUpload_2: ImageView? = null
    private var documentUpload_3: ImageView? = null
    private var documentUpload_4: ImageView? = null

    private var image_1: String? = null
    private var image_2: String? = null
    private var image_3: String? = null
    private var image_4: String? = null
    private var passingYear: String? = (null).toString()
    private var uploadPDF: String? = null


    companion object {
        private const val picId_1 = 11
        private const val picId_2 = 22
        private const val picId_3 = 33
        private const val picId_4 = 44
        private const val REQUEST_CODE_PDF = 71

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = AdvanceBinding.inflate(inflater, container, false)
        dataBase = DatabaseBuilder.getInstance(requireContext())
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

            if (image_1 == null || image_2 == null || image_3 == null || image_4 == null) {
                Toast.makeText(requireContext(), "All image files are required", Toast.LENGTH_SHORT)
                    .show()
            } else if (uploadPDF == null) {
                Toast.makeText(requireContext(), "Upload PDF file", Toast.LENGTH_SHORT).show()

            } else if (passingYear == null || passingYear == "Select Year:") {
                Toast.makeText(requireContext(), "Select Passing year", Toast.LENGTH_SHORT).show()
            } else {

                val progressBar = binding.spinKit as ProgressBar
                val Circle: Sprite = Circle()
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateDrawable = Circle

//                BasicFragment.checkFormSubmit = true;
                val AdvanceData = Advance(
                    image_1 = image_1,
                    image_2 = image_2,
                    image_3 = image_3,
                    image_4 = image_4,
                    uploadPDF = uploadPDF,
                    passingYear = passingYear
                )
                Handler(Looper.getMainLooper()).postDelayed({

                    lifecycleScope.launch {
                        dataBase.userDao().insertAdvance(AdvanceData)
                    }
                    Toast.makeText(requireContext(), "Data Successfully stored", Toast.LENGTH_SHORT)
                        .show()
                    progressBar.visibility = View.GONE
                }, 2000)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PDF && resultCode == Activity.RESULT_OK) {

            val selectedPdfUri = data?.data

            val pdfFileName = getFileName(selectedPdfUri)
            showPdfPreview(pdfFileName)
        }
        if (requestCode == picId_1 && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap
            val myPhoto = imageBitmap?.let { compressBitmap(it, 100) }
            val documetUpload_1: ImageView? = view?.findViewById(R.id.document_1)

            documetUpload_1?.setImageBitmap(myPhoto)
            image_1 = myPhoto.toString()

        }
        if (requestCode == picId_2 && resultCode == Activity.RESULT_OK) {
            // Get the captured image from the intent
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            val myPhoto = imageBitmap?.let { compressBitmap(it, 100) }

            // Assuming you have an ImageView in your layout with the ID cnicBackImageView
            val documenUpload_2: ImageView? = view?.findViewById(R.id.document_2)

            // Set the captured image on your ImageView
            documenUpload_2?.setImageBitmap(myPhoto)

            image_2 = myPhoto.toString()
        }
        if (requestCode == picId_3 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                val bitmap = uriToBitmap(imageUri!!)
                val myPhoto = bitmap?.let { compressBitmap(it, 100) }
                documentUpload_3?.setImageBitmap(myPhoto)
                image_3 = myPhoto.toString()

            } else {
                // Handle case where data is null
            }
        } else if (requestCode == picId_4 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {

                val imageUri = data.data
                val bitmap = uriToBitmap(imageUri!!)
                val myPhoto = bitmap?.let { compressBitmap(it, 100) }
                documentUpload_4?.setImageBitmap(myPhoto)
                image_4 = myPhoto.toString()

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
                passingYear = yearPassing_spinner.toString()
                if (position != 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " + "" + yearPassingSpinner[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                passingYear = null
            }
        }
    }

    private fun uploadDocument() {

        documentUpload_3?.setOnClickListener(View.OnClickListener { _: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, AdvanceFragment.picId_3)
        })
        documentUpload_4?.setOnClickListener(View.OnClickListener { _: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, AdvanceFragment.picId_4)
        })
    }

    private fun chooseMedia() {
        binding.uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, REQUEST_CODE_PDF)
        }
    }

    private fun getFileName(uri: Uri?): String {
        var result = ""
        uri?.let {
            val cursor: Cursor? = activity?.contentResolver?.query(it, null, null, null, null)
            cursor?.use { it ->
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
        uploadPDF = pdfFileName.toString()
        Toast.makeText(context, "Selected PDF: $pdfFileName", Toast.LENGTH_SHORT).show()
    }

    private fun openCamera(requestCode: Int) {
        // Open the camera using an intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
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
                requireContext(), android.Manifest.permission.CAMERA
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

    //No usage
    //    fun generateUniqueImageName(): String {
//        // Use timestamp to ensure uniqueness
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//
//        // Generate a unique identifier (UUID) to further ensure uniqueness
//        val uniqueId = UUID.randomUUID().toString()
//
//        // Combine timestamp and unique identifier to create a unique name
//        return "IMG_$timeStamp$uniqueId.jpg"
//    }
    fun compressBitmap(inputBitmap: Bitmap, quality: Int): Bitmap {
        val outputStream = ByteArrayOutputStream()
        inputBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor =
                requireContext().contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}


