package com.volvadvit.kotlinmessenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.*
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.auth.LoginActivity
import com.volvadvit.kotlinmessenger.common.*
import com.volvadvit.kotlinmessenger.views.LastMessageItem
import com.volvadvit.kotlinmessenger.model.Message
import com.volvadvit.kotlinmessenger.model.User
import com.volvadvit.kotlinmessenger.views.UserListActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_messages_list.*

class MessagesListActivity : AppCompatActivity() {

    private val mAdapter = GroupAdapter<GroupieViewHolder>()
    private val lastMessageMap = HashMap<String, Message>()

    companion object {
        var mUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages_list)
        supportActionBar?.title = "Messages"

        checkAuthState()
        fetchCurrentUser()
        listeningLatestMessages()
        mAdapter.setOnItemClickListener { item, view ->
            onClickMessage(item as LastMessageItem)
        }
        recycler_latest_messages.adapter = mAdapter
        recycler_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun onClickMessage(item: LastMessageItem) {
        val user = item.oppositeUser
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("USER_KEY", user)
        startActivity(intent)
    }

    private fun listeningLatestMessages() {
        if (mAuth.currentUser != null) {
            val fromId = mAuth.currentUser?.uid
            mDataBase.child("latest-messages")
                .child(fromId!!)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val message = snapshot.getValue(Message::class.java) ?: return
                        lastMessageMap[snapshot.key!!] = message
                        recyclerRefresh()
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        val message = snapshot.getValue(Message::class.java) ?: return
                        lastMessageMap[snapshot.key!!] = message
                        recyclerRefresh()
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun recyclerRefresh() {
        mAdapter.clear()
        lastMessageMap.values.forEach {
            mAdapter.add(LastMessageItem(it))
        }
    }

    private fun fetchCurrentUser() {
        mDataBase.child("users").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        if (user?.uid == mAuth.currentUser?.uid) {
                            mUser = user
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun checkAuthState() {
        mAuth.addAuthStateListener {
            if (it.currentUser == null || it.uid == null) {
                showToast("Please, enter email and password")
                Log.d(
                    "MainActivity: onStart",
                    "Auth validate exception, user uid : $currentUserUid"
                )
                startNewTaskIntent(LoginActivity())
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
            R.id.menu_new_message -> { startIntent(UserListActivity()) }
            R.id.menu_sign_out -> { mAuth.signOut() }
        }
        return super.onOptionsItemSelected(item)
    }
}