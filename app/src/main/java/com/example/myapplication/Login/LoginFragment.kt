package com.example.myapplication.Login


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment(R.layout.login_fragment), View.OnClickListener {

    lateinit var navController: NavController
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        loginButton.setOnClickListener(this)
        forgot_pw.setOnClickListener(this)
        create_acc.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val email = email_input.text.toString().trim()
        val password = pwInput.text.toString().trim()
        when (v) {
            loginButton -> signInWithEmailAndPassword(email, password)
            create_acc -> navController.navigate(R.id.action_loginFragment_to_registrationFragment)
            forgot_pw -> navController.navigate(R.id.action_fragment_nav_login_to_forgotResetPasswordFragment)
        }
}

    fun signInWithEmailAndPassword(email: String, password: String) {
        progressBar_login.visibility = View.VISIBLE
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginButton.isClickable=false
            create_acc.isClickable=false
            forgot_pw.isClickable=false
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        Toast.makeText(
                            this.activity, "Welcome! ${user!!.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                       // activity!!.onBackPressed()
                        //val intent = Intent(this.activity, MainActivity::class.java)
                      //  startActivity(intent)
                        activity!!.finish()
                    } else {
                        if (!task.isSuccessful) {
                            try {
                                throw task.exception!!
                            } catch (e: Exception) {
                                Toast.makeText(this.activity, "${e.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        //    Log.w(TAG, "signInWithEmail:failure", task.exception)
                        //   Toast.makeText(
                        //      this.activity, "Password incorrect",
                        //      Toast.LENGTH_SHORT
                        //   ).show()
                        loginButton.isClickable=true
                        create_acc.isClickable=true
                        forgot_pw.isClickable=true
                    }
                    // [START_EXCLUDE]
                    //  hideProgressBar()
                    // [END_EXCLUDE]
                }
            // [END sign_in_with_email]
            // [END create_user_with_email]
        } else {
            Toast.makeText(activity, "Please input email and password", Toast.LENGTH_SHORT)
                .show()
        }
        progressBar_login.visibility = View.INVISIBLE
    }

    companion object

    val TAG = "Fragment_nav_login"
}

