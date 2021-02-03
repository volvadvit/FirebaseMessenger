package com.volvadvit.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        startActivity(Intent(this, RegisterActivity::class.java))
            finish()
//        mAuth.addAuthStateListener {
//            if (it.currentUser == null) {
//                Log.d("!@#", "$currentUserUid")
//                startActivity(Intent(this, RegisterActivity::class.java))
//                finish()
//            } else {
//                // do something with user
//            }
//        }
    }
}