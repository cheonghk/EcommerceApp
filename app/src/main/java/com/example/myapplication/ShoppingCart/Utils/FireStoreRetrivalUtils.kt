package com.example.myapplication.ShoppingCart.Utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object FireStoreRetrivalUtils {
    fun mFirebaseFirestore(uid:String): CollectionReference {
        return FirebaseFirestore.getInstance().collection("shoppingcart")
            .document(uid).collection("Items")
    }
}