package com.example.myapplication.ShoppingCart.Utils

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreUtils {
    fun mFirebaseFirestore(uid:String):DocumentReference {
        return FirebaseFirestore.getInstance().collection("shoppingcart")
            .document(uid).collection("Items").document()
    }
}