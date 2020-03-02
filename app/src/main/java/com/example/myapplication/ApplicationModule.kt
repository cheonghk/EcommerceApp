package com.example.myapplication

import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides


@Module
class ApplicationModule {



    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore? {
        return FirebaseFirestore.getInstance()
    }

    @Nullable
    @Provides
    fun provideUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

}