package com.example.taskclass.Fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taskclass.R
import com.example.taskclass.databinding.AdvanceBinding

class Advance : Fragment() {
    private lateinit var binding: AdvanceBinding
    private var yearPassing_spinner: String? = null

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




        uploadDocument();


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val selectedPdfUri = data?.data

            val pdfFileName = getFileName(selectedPdfUri)

            showPdfPreview(pdfFileName)
        }

        if (requestCode == picId_1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val photo = BitmapFactory.decodeStream(inputStream)
                    documentUpload_1?.setImageBitmap(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Handle case where data is null
            }
        } else if (requestCode == picId_2 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val imageUri = data.data
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val photo = BitmapFactory.decodeStream(inputStream)
                    documentUpload_2?.setImageBitmap(photo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                // Handle case where data is null
            }
        } else if (requestCode == picId_3 && resultCode == Activity.RESULT_OK) {
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
        documentUpload_1?.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_1)
        })
        documentUpload_2?.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_2)
        })
        documentUpload_3?.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_3)
        })
        documentUpload_4?.setOnClickListener(View.OnClickListener { v: View? ->
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Advance.picId_4)
        })
    }

    private fun chooseMedia() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, REQUEST_CODE)
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
}

