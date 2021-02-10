package com.volvadvit.kotlinmessenger.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.volvadvit.kotlinmessenger.views.MessagesListActivity
import de.hdodenhof.circleimageview.CircleImageView

fun Activity.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Activity.startNewTaskIntent(activity: Activity) {
    val intent = Intent(this, activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.startIntent(activity: Activity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
}


fun CircleImageView.uploadUserPhoto(url: String) {
    if (url != "") {
        Picasso.get()
            .load(url)
            .into(this)
    }
}