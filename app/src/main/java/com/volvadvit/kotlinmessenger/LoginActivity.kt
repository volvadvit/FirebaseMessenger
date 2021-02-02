package com.volvadvit.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        login_clickable_text.setOnClickListener {
            startActivity(
                    Intent(this, RegisterActivity::class.java)
            )
            finish()
        }
    }
}