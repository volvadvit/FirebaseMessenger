package com.volvadvit.kotlinmessenger.views

import com.volvadvit.kotlinmessenger.R
import com.volvadvit.kotlinmessenger.common.uploadUserPhoto
import com.volvadvit.kotlinmessenger.model.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_chat_my.view.*

class ChatItemMy(val text: String, val user: User): Item<GroupieViewHolder>() {

    override fun getLayout(): Int = R.layout.item_chat_my
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.output_text.text = text
        loadUserPhoto(user, viewHolder.itemView.photo)
    }

    private fun loadUserPhoto(user: User, imageView: CircleImageView) {
        if (user.imageUrl != "") {
            imageView.uploadUserPhoto(user.imageUrl)
        }
    }
}