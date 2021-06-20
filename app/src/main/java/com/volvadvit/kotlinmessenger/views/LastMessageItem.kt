package com.volvadvit.kotlinmessenger.views

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.mAuth
import com.volvadvit.kotlinmessenger.common.mDataBase
import com.volvadvit.kotlinmessenger.common.uploadUserPhoto
import com.volvadvit.kotlinmessenger.model.Message
import com.volvadvit.kotlinmessenger.model.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_last_message.view.*

class LastMessageItem(val message: Message): Item<GroupieViewHolder>() {

    var oppositeUser: User? = null

    override fun getLayout() = R.layout.item_last_message

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.last_message.text = message.text
        viewHolder.itemView.last_time.text = message.timeStamp.substring(11,16 )

        val chatPartnerId = if (message.fromId == mAuth.currentUser?.uid) {
            message.toId
        } else {
            message.fromId
        }
        mDataBase.child("users")
            .child(chatPartnerId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                oppositeUser = snapshot.getValue(User::class.java) ?: return
                viewHolder.itemView.last_name.text = oppositeUser?.username
                viewHolder.itemView.last_image.uploadUserPhoto(oppositeUser?.imageUrl!!)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}