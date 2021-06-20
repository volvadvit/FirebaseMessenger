package com.volvadvit.kotlinmessenger.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.mDataBase
import com.volvadvit.kotlinmessenger.common.uploadUserPhoto
import com.volvadvit.kotlinmessenger.model.User
import com.volvadvit.kotlinmessenger.common.showToast
import com.volvadvit.kotlinmessenger.messages.ChatActivity
import com.volvadvit.kotlinmessenger.messages.MessagesListActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.item_user.view.*

class UserListActivity : AppCompatActivity() {

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.title = "Select User" // get action bar from AppCompatActivity
        fetchUser()
    }

    private fun fetchUser() {
        // Get users from database and load it into GroupAdapter<GroupieViewHolder>()
        mDataBase.child("users")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val adapter = GroupAdapter<GroupieViewHolder>()
                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)    // get value like User's instance variable
                        if (user?.uid != MessagesListActivity.mUser?.uid) user?.let { adapter.add(UserListItem(user))
                        }
                        adapter.setOnItemClickListener { item, view ->

                            val userItem = item as UserListItem
                            val intent = Intent(view.context, ChatActivity::class.java)
                            intent.putExtra(USER_KEY, userItem.user)
                            startActivity(intent)
                        }
                    }
                    user_list_recycler.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    this@UserListActivity.showToast("Database error")
                }
            })
    }
}

class UserListItem(val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int = R.layout.item_user
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.item_user_username.text = user.username
        viewHolder.itemView.item_user_photo.uploadUserPhoto(user.imageUrl)
    }
}

