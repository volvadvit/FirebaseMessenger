package com.volvadvit.kotlinmessenger.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.*

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        startNewTaskIntent(RegisterActivity())
            if (currentUser == null || mAuth.uid == null) {
                showToast("Please, enter email and password")
                Log.d("MainActivity: onStart", "Auth validate exception, user uid : $currentUserUid")
                startNewTaskIntent(LoginActivity())
                finish()
            } else {
                startNewTaskIntent(MessagesListActivity())
                finish()
            }
    }
}