package com.volvadvit.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.mAuth
import com.volvadvit.kotlinmessenger.common.mDataBase
import com.volvadvit.kotlinmessenger.views.ChatItemMy
import com.volvadvit.kotlinmessenger.views.ChatItemPerson
import com.volvadvit.kotlinmessenger.model.Message
import com.volvadvit.kotlinmessenger.model.User
import com.volvadvit.kotlinmessenger.views.UserListActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    var oppositeUser: User? = null

    private val mAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        oppositeUser = intent.getParcelableExtra<User>(UserListActivity.USER_KEY)
        supportActionBar?.title = oppositeUser?.username
        chat_recycler.adapter = mAdapter

        listeningForMessages()

        chat_send_btn.setOnClickListener {
            validateAndSendMessage()
        }
    }

    private fun listeningForMessages() {
        mDataBase.child("user-messages")
            .child(mAuth.currentUser?.uid!!)
            .child(oppositeUser?.uid!!)
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java) ?: return
                when (message.fromId) {
                    mAuth.currentUser?.uid -> {
                        mAdapter.add(ChatItemMy(message.text, MessagesListActivity.mUser!!))
                    }
                    else -> {
                        oppositeUser = intent.getParcelableExtra(UserListActivity.USER_KEY)
                        mAdapter.add(ChatItemPerson(message.text, oppositeUser!!))
                    }
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun validateAndSendMessage() {
        val message = chat_send_text.text
        var outgoingMessage = ""
        if (!message.isNullOrEmpty() && message.length <= 100) {
            outgoingMessage = message.toString()
        } else if (message.length > 100) {
                outgoingMessage = message.toString().subSequence(0,99).toString()
        }
        if (outgoingMessage != "") {
            chat_send_text.text.clear()
            saveMessageToFirebase(outgoingMessage)
        }
    }

    private fun saveMessageToFirebase(text: String) {
        val fromId = mAuth.currentUser?.uid!!
        val toId = oppositeUser!!.uid

        val userMessageRef = mDataBase.child("user-messages")
            .child(fromId)
            .child(toId)
            .push()

        val oppositeMessageRef = mDataBase.child("user-messages")
            .child(toId)
            .child(fromId)
            .push()

        oppositeUser = intent.getParcelableExtra<User>(UserListActivity.USER_KEY)
        if (mAuth.currentUser?.uid == null || oppositeUser == null) return

        val timeStamp = getTimeStamp()
        val message = Message(userMessageRef.toString(), text, fromId, toId, timeStamp)

        userMessageRef.setValue(message)
        oppositeMessageRef.setValue(message)

        //lastMessageRef
        mDataBase.child("latest-messages")
            .child(fromId)
            .child(toId)
            .setValue(message)

        //lastMessageOppositeRef
        mDataBase.child("latest-messages")
            .child(toId)
            .child(fromId)
            .setValue(message)
    }

    private fun getTimeStamp(): String {
        var timeStamp = ""
        try {
            timeStamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(
                    ZoneId.systemDefault()
                )
            )
        } catch (e: Exception) {
            Log.e("UnsupportedTemporalTypeException", "Can't get the timezone ::: ${e.message}")
        }
        return timeStamp
    }
}

