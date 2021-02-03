package com.volvadvit.kotlinmessenger.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        startActivity(Intent(this, RegisterActivity::class.java))
            if (currentUser == null || mAuth.uid == null) {
                showToast("Please, enter email and password")
                Log.d("MainActivity: onStart", "Auth validate exception, user uid : $currentUserUid")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startIntent(MessagesListActivity())
                finish()
            }
    }
}