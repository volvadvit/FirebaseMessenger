package com.volvadvit.kotlinmessenger.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.*
import com.volvadvit.kotlinmessenger.model.User
import com.volvadvit.kotlinmessenger.messages.MessagesListActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var userPhotoUri: Uri = "".toUri()
    private var photoUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_select_photo_btn.setOnClickListener {
            selectImageToUpload()
        }
        register_select_photo_view.visibility = View.INVISIBLE
        register_select_photo_view.setOnClickListener {
            selectImageToUpload()
        }

        register_btn.setOnClickListener {
            validateRegisterData()
        }

        register_clickable_text.setOnClickListener {
            startNewTaskIntent(LoginActivity())
            finish()
        }
    }

    private fun selectImageToUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)   // invoke method onActivityResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            userPhotoUri = data.data ?: return  // data.getExtra()
            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, userPhotoUri)    // map of pixels
            register_select_photo_view.setImageBitmap(bitmap)

            register_select_photo_view.visibility = View.VISIBLE
            register_select_photo_btn.visibility = View.INVISIBLE
        } else {
            showToast("Image upload error. Try again")
        }
    }

    private fun validateRegisterData() {
        val email = register_editText_email.text.toString()
        val password = register_editText_password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuthRegisterUser(email, password)
        } else {
            showToast("Please, enter email and password")
        }
    }

    private fun mAuthRegisterUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d("Reg", "Create User With Email: success ${mAuth.currentUser?.uid}")
                    uploadPhotoToFirebase()
                } else {
                    this.showToast(it.exception!!.message!!)
                    Log.d("Reg", "Not successful create" + it.exception!!.message!!)
                }
            }.addOnFailureListener(this) {
                showToast("RegFailureListener: " + it.message!!)
            }
    }

    private fun uploadPhotoToFirebase() {
        if (userPhotoUri.toString().isNotEmpty() && mAuth.currentUser?.uid != null) {
            mStorage.child("images").child(mAuth.currentUser?.uid!!).putFile(userPhotoUri)
                .addOnSuccessListener {
                    it?.storage?.downloadUrl?.addOnSuccessListener { uri ->
                        photoUrl = uri.toString()
                        saveUserToDatabase()
                    }
                }
                .addOnFailureListener {
                    showToast("Fail upload photo: ${it.message!!}")
                    Log.d("Reg", it.message!!)
                }
        } else {
            Log.d("Reg", "Photo uri is null")
            showToast("Set default profile image")
            register_select_photo_view.visibility = View.VISIBLE
            register_select_photo_btn.visibility = View.INVISIBLE
            saveUserToDatabase()
        }
    }

    private fun saveUserToDatabase() {
        if (mAuth.currentUser?.uid != null) {
            val user = User(mAuth.currentUser!!.uid, register_editText_username.text.toString(), photoUrl)
            mDataBase.child("users").child(mAuth.currentUser!!.uid)
                .setValue(user)
            startNewTaskIntent(MessagesListActivity())
        }
    }
}
