package com.example.myapplication.Login


import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
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
    var timer : CountDownTimer? = null
    var endTime :Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (endTime != null) {
            Log.i(TAG, "${endTime}")
            outState.putLong(EndTime, endTime!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            endTime = savedInstanceState.getLong(EndTime)
            Log.i(TAG, "${endTime}")
            if (endTime != null) {
                if (endTime!! - System.currentTimeMillis() > 2000) {
                    sendresetpwbttn.isClickable = false
                    sendresetpwbttn.alpha = 0.3F
                    timer?.start()
                }
            }
        }
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
            sendresetpwbttn.isClickable=false
            sendresetpwbttn.alpha = 0.3F
            // sendresetpwbttn.setText("(${System.currentTimeMillis()})")
            val sendTime = System.currentTimeMillis()
            endTime = sendTime+60000
            //var finishTime = endTime!!-sendTime
            timer = object : CountDownTimer(endTime!!-System.currentTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    sendresetpwbttn.setText("(${millisUntilFinished/1000})")
                }
                override fun onFinish() {
                    sendresetpwbttn.setText("Send Email")
                    sendresetpwbttn.isClickable=true
                    sendresetpwbttn.alpha = 1F
                    endTime = null
                }
            }
            timer!!.start()

            mAuth!!.sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener { task ->
                    // progressDialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(activity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT).show()
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

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    companion object {
        val TAG = "ForgotResetPasswordFragment"
        val EndTime = "endTime"
    }
}
