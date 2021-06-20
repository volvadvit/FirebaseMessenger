package com.volvadvit.kotlinmessenger.model

data class Message(val id: String = "", val text: String = "",
              val fromId: String = "", val toId: String = "", val timeStamp: String = "") {
}