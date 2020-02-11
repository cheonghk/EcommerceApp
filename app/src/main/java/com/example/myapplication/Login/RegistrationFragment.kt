package com.example.myapplication.Login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.Main.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.android.synthetic.main.registration_fragment.*
import org.jetbrains.anko.startActivity


class RegistrationFragment : Fragment(R.layout.registration_fragment), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_button.setOnClickListener(this)
        cancel_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
            val email = email_re.text.toString().trim()
            val password = password_re.text.toString().trim()
            val confirm_password = confirm_password_re.text.toString().trim()
            when (v) {
                register_button -> createUserWithEmailAndPassword(email, password, confirm_password)
                cancel_button -> activity!!.onBackPressed()
                // forgot_pw ->
            }

    }


    fun createUserWithEmailAndPassword(email: String, password: String, confirm_password: String) {
        email_re.error = null
        password_re.error = null
        confirm_password_re.error = null
            if (TextUtils.isEmpty(email)) {
                email_re.error = "Please input email"
                email_re.performClick()
                email_re.requestFocus()
                return
            }
            if (TextUtils.isEmpty(password)) {
                password_re.error = "Please input password"
                password_re.performClick()
                password_re.requestFocus()
                return
            }
            if (TextUtils.isEmpty(confirm_password)) {
                confirm_password_re.error = "Input password again"
                confirm_password_re.performClick()
                confirm_password_re.requestFocus()
                return
            }
            if (!password.equals(confirm_password)) {
                Toast.makeText(this.activity, "Confirm password do not match", Toast.LENGTH_SHORT)
                    .show()
                confirm_password_re.requestFocus()
                return
            }
            cancel_button.isClickable = false
            register_button.isClickable = false
            progressBar_registration.visibility = View.VISIBLE
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this.activity, "Create account sucessful", Toast.LENGTH_SHORT)
                        .show()
                    Log.w(TAG, "createUserWithEmail:success", task.exception)
                    val user = mAuth.currentUser
                    val intent = Intent(this.activity, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                } else {
                    try {
                        throw task.exception!!
                    } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                        Toast.makeText(
                            this.activity,
                            "Password must not be shorter than 6 words",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this.activity, "Malformed email", Toast.LENGTH_SHORT).show()
                    } catch (existEmail: FirebaseAuthUserCollisionException) {
                        email_re.error = "This email was regitstered"
                        email_re.requestFocus()
                    } catch (e: Exception) {
                        Toast.makeText(this.activity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                // [START_EXCLUDE]
                //   hideProgressBar()
                // [END_EXCLUDE]
            }
            // [END create_user_with_email]
            cancel_button.isClickable = true
            register_button.isClickable = true
            progressBar_registration.visibility = View.INVISIBLE
        }

    companion object val TAG = "RegistrationFragment "
}


