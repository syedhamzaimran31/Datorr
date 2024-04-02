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
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.Datorr.R
import com.example.Datorr.databinding.BasicBinding
import com.example.Datorr.room.AppDatabase
import com.example.Datorr.room.Basic
import com.example.Datorr.room.DatabaseBuilder
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class BasicFragment : Fragment() {

    private val REQUEST_CODE = 50
    private val REQUEST_CODE_CAMERA_1 = 51
    private val REQUEST_CODE_CAMERA_2 = 52
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var database: AppDatabase
    private var cnicFront: String? = null
    private var cnicBack: String? = null
    private var pdf_room: String? = null

    companion object {
        public var checkFormSubmit = false
    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding: BasicBinding = BasicBinding.inflate(inflater, container, false)
        database = DatabaseBuilder.getInstance(requireContext())

//        val imagePath = ""
//        val bitmap = BitmapFactory.decodeFile(imagePath)

        val imageView_1: ImageView = binding.cnicFront
        val imageView_2:ImageView=binding.cnicBack
//        imageView_1.setImageBitmap(bitmap)
//        imageView_2.setImageBitmap(bitmap)

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

            val cnicNumber = binding.cnicNumber.text.toString().trim();
            val phoneNumber = binding.phoneNumber.text.toString().trim();

            if (TextUtils.isEmpty(binding.cnicNumber.text.toString().trim())) {
                binding.cnicNumber.setError("CNIC number cannot be empty")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.phoneNumber.text.toString().trim())) {
                binding.phoneNumber.setError("Phone number can not be empty")
                return@setOnClickListener

            }
            if (cnicFront == null || cnicBack == null) {

                Toast.makeText(
                    requireContext(),
                    "Upload CNIC Front and Back Image",
                    Toast.LENGTH_SHORT
                ).show()

            }
            if (pdf_room == null) {
                Toast.makeText(
                    requireContext(),
                    "Upload PDF File",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val progressBar = binding.spinKit as ProgressBar
                val Circle: Sprite = Circle()
                progressBar.visibility = View.VISIBLE
                progressBar.indeterminateDrawable = Circle

                val BasicData = Basic(
                    cnicFront = cnicFront,
                    cnicBack = cnicBack,
                    cnicNumber = cnicNumber,
                    phoneNumber = phoneNumber,
                    selectPDF = pdf_room
                )

                Handler(Looper.getMainLooper()).postDelayed({
                lifecycleScope.launch {
                    database.userDao().insertBasic(BasicData)
                }
                Toast.makeText(context, "Data successfully stored", Toast.LENGTH_SHORT).show()
                checkFormSubmit = true;
                    progressBar.visibility = View.GONE
                }, 2000)
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
            val myPhoto= imageBitmap?.let { compressBitmap(it,100) }

            val cnicFrontImageView: ImageView? = view?.findViewById(R.id.cnicFront)
            val selectedImageUri: Uri? = data?.data
//            selectedImagePath = generateUniqueImageName()
            cnicFrontImageView?.setImageBitmap(myPhoto)
//            cnicFront = encodeBitmapToBase64(imageBitmap)
            cnicFront = myPhoto.toString()

        }
        if (requestCode == REQUEST_CODE_CAMERA_2 && resultCode == Activity.RESULT_OK) {
            // Get the captured image from the intent
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap

            val myPhoto= imageBitmap?.let { compressBitmap(it,100) }
            // Assuming you have an ImageView in your layout with the ID cnicBackImageView
            val cnicBackImageView: ImageView? = view?.findViewById(R.id.cnicBack)

            // Set the captured image on your ImageView
            cnicBackImageView?.setImageBitmap(myPhoto)
//            cnicBack = encodeBitmapToBase64(imageBitmap)
            cnicBack = myPhoto.toString()
        }
    }

    private fun getFileName(uri: Uri?): String {
        var result = ""
        uri?.let { it ->
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
        val imageViewPdfPreview: ImageView? = view?.findViewById(R.id.imageviewBasic_pdf)

        // Set a placeholder image or PDF icon as the preview
        imageViewPdfPreview?.setImageResource(R.drawable.pdf)
        imageViewPdfPreview?.visibility = View.VISIBLE;
        // Optionally, you can also display the PDF file name in a Toast
        Toast.makeText(context, "Selected PDF: $pdfFileName", Toast.LENGTH_SHORT).show()
        pdf_room = pdfFileName.toString()
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

//    private fun encodeBitmapToBase64(bitmap: Bitmap?): String {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }

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
//    fun saveImage(bitmap: Bitmap?): String? {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
//    }
    fun compressBitmap(inputBitmap: Bitmap, quality: Int): Bitmap {
        val outputStream = ByteArrayOutputStream()
        inputBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
