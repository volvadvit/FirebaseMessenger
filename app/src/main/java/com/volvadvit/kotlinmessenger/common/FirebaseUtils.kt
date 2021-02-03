package com.volvadvit.kotlinmessenger.common

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

val mAuth = FirebaseAuth.getInstance()
val mRef = FirebaseDatabase.getInstance().reference
val mStorage = FirebaseStorage.getInstance().reference

val currentUser = mAuth.currentUser
val currentUserUid = currentUser?.uid