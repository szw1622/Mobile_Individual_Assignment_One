package com.example.app1

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.ActivityInfo
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val STATE_FIRST_NAME = "firstName"
        private const val STATE_MIDDLE_NAME = "middleName"
        private const val STATE_LAST_NAME = "lastName"
        private const val STATE_THUMBNAIL = "thumbnail"
    }

    private lateinit var submitButton: Button
    private lateinit var photoButton: Button
    private lateinit var firstNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var thumbnailImageView: ImageView

    private var firstName: String? = null
    private var middleName: String? = null
    private var lastName: String? = null
    private var thumbnail: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        submitButton = findViewById(R.id.submitDataButton)
        photoButton = findViewById(R.id.capturePhotoButton)
        firstNameEditText = findViewById(R.id.firstNameEditText)
        middleNameEditText = findViewById(R.id.middleNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        thumbnailImageView = findViewById(R.id.thumbnailImageView)

        photoButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        submitButton.setOnClickListener {
            firstName = firstNameEditText.text.toString()
            middleName = middleNameEditText.text.toString()
            lastName = lastNameEditText.text.toString()

            if (firstName!!.isEmpty() || lastName!!.isEmpty()) {
                Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SecondPage::class.java)
                intent.putExtra("fullName", getFullName())
                startActivity(intent)
            }
        }

        if (savedInstanceState != null) {
            firstName = savedInstanceState.getString(STATE_FIRST_NAME)
            middleName = savedInstanceState.getString(STATE_MIDDLE_NAME)
            lastName = savedInstanceState.getString(STATE_LAST_NAME)
            thumbnail = savedInstanceState.getParcelable(STATE_THUMBNAIL)
            firstNameEditText.setText(firstName)
            middleNameEditText.setText(middleName)
            lastNameEditText.setText(lastName)
            thumbnailImageView.setImageBitmap(thumbnail)
        }
    }

    private fun getFullName(): String {
        val fullNameBuilder = StringBuilder()
        fullNameBuilder.append(firstName.orEmpty())
        fullNameBuilder.append(middleName?.ifEmpty { "" }.let { if (fullNameBuilder.isNotEmpty()) " $it" else it })
        fullNameBuilder.append(lastName.orEmpty().let { if (fullNameBuilder.isNotEmpty()) " $it" else it })
        return fullNameBuilder.toString()
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val thumbnailBitmap = data?.extras?.get("data") as? Bitmap
            if (thumbnailBitmap != null) {
                thumbnail = thumbnailBitmap
                thumbnailImageView.setImageBitmap(thumbnail)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_FIRST_NAME, firstName)
        outState.putString(STATE_MIDDLE_NAME, middleName)
        outState.putString(STATE_LAST_NAME, lastName)
        outState.putParcelable(STATE_THUMBNAIL, thumbnail)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        firstName = savedInstanceState.getString(STATE_FIRST_NAME)
        middleName = savedInstanceState.getString(STATE_MIDDLE_NAME)
        lastName = savedInstanceState.getString(STATE_LAST_NAME)
        thumbnail = savedInstanceState.getParcelable(STATE_THUMBNAIL)
        firstNameEditText.setText(firstName)
        middleNameEditText.setText(middleName)
        lastNameEditText.setText(lastName)
        thumbnailImageView.setImageBitmap(thumbnail)
    }
}
