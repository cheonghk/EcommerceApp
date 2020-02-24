package com.example.myapplication.Login


import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
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
    var endTime :Long? = null
    var mView : View? = null
    var timer : CountDownTimer?  = null
    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
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
            setTimer()

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

    fun setTimer() {
        sendresetpwbttn.isClickable = false
        sendresetpwbttn.alpha = 0.3F
         timer = object : CountDownTimer(endTime!! - System.currentTimeMillis(), 1000) {
            val sendresetpwbttn = mView?.findViewById<Button>(R.id.sendresetpwbttn)
            override fun onTick(millisUntilFinished: Long) {
                sendresetpwbttn?.setText("(${millisUntilFinished / 1000})")
            }

            override fun onFinish() {
                sendresetpwbttn?.setText("Send Email")
                sendresetpwbttn?.isClickable = true
                sendresetpwbttn?.alpha = 1F
                endTime = null
                bundle.clear()
            }
        }
        timer?.start()
    }


    override fun onPause() {
        super.onPause()
        if (endTime != null) {
            bundle.putLong(EndTime, endTime!!)
            Log.i(TAG, "putttt"+"${endTime}")
        }
    }

    override fun onStart() {
        super.onStart()
        if(bundle!=null){
        endTime = bundle.getLong(EndTime)}
            Log.i(TAG, "endTime : "+"${endTime}")
        setTimer()
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
