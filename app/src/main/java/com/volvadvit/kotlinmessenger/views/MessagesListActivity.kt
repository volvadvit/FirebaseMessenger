package com.volvadvit.kotlinmessenger.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.*
import com.volvadvit.kotlinmessenger.model.User

class MessagesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_list)

        checkAuthState()
    }

    private fun checkAuthState() {
        mAuth.addAuthStateListener {
            if (it.currentUser == null || it.uid == null) {
                showToast("Please, enter email and password")
                Log.d(
                    "MainActivity: onStart",
                    "Auth validate exception, user uid : $currentUserUid"
                )
                startIntent(LoginActivity())
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Make top toolbox in action bar
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> { startActivity(Intent(this, UserListActivity::class.java)) }
            R.id.menu_sign_out -> { mAuth.signOut() }
        }
        return super.onOptionsItemSelected(item)
    }
}