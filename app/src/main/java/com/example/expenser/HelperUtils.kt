package com.example.expenser

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object HelperUtils {
    fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}