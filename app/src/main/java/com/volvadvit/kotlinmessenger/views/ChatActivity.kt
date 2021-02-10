package com.volvadvit.kotlinmessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<User>(UserListActivity.USER_KEY)
        supportActionBar?.title = user!!.username

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatItemPerson())
        adapter.add(ChatItemMy())
        adapter.add(ChatItemPerson())
        chat_recycler.adapter = adapter
    }
}

class ChatItemPerson(): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int = R.layout.item_chat_person

}

class ChatItemMy(): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int = R.layout.item_chat_my

}