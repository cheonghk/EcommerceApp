package com.example.myapplication.Login


import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forgot_reset_password.*


/**
 * A simple [Fragment] subclass.
 */
class ForgotResetPasswordFragment : Fragment(R.layout.fragment_forgot_reset_password),
    View.OnClickListener {


    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendresetpwbttn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val inputEmail = inputEmail.text.toString().trim()

        when (v) {
            sendresetpwbttn -> sendResetEmail(inputEmail)
        }
    }


    fun sendResetEmail(inputEmail: String) {
        if (!TextUtils.isEmpty(inputEmail)) {
            mAuth!!.sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener { task ->
                    // progressDialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(activity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT).show()
                       // sendresetpwbttn.isClickable=false
                        sendresetpwbttn.isClickable=false
                        object : CountDownTimer(60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                sendresetpwbttn.setText("(${millisUntilFinished/1000})")
                            }
                            override fun onFinish() {
                                sendresetpwbttn.setText("Send Email")
                                sendresetpwbttn.isClickable=true
                            }
                        }.start()
                    } else {
                        Toast.makeText(
                            activity,
                            "Failed to send reset email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
