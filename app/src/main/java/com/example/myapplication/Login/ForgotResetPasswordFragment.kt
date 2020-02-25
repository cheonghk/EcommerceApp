package com.example.myapplication.Login

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
    lateinit var mContext : Context
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

    }

    override fun onClick(v: View?) {
        val inputEmail = inputEmail.text.toString().trim()
        when (v) {
            sendresetpwbttn -> sendResetEmail(inputEmail)
        }
    }

    fun sendResetEmail(inputEmail: String) {
        if (!TextUtils.isEmpty(inputEmail)) {
            val sendTime = System.currentTimeMillis()
            endTime = sendTime + 60000

            mAuth.sendPasswordResetEmail(inputEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        diableBttnTimer()
                        Toast.makeText(
                            activity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (mFirebaseAuthInvalidUserException: FirebaseAuthInvalidUserException) {
                            Toast.makeText(
                                activity,
                                "Email not exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this.activity, "Malformed email", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: Exception) {
                            Log.i(TAG, "${e.message}")
                            Toast.makeText(
                                this.activity,
                                "Failed to send reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    fun diableBttnTimer() {
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
            }
        }
        timer?.start()
    }

    override fun onPause() {
        super.onPause()
        if (endTime != null) {
            val prefs = mContext.getSharedPreferences(EndTimePrefs, MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong(EndTime, endTime!!)
            editor.apply()
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = mContext.getSharedPreferences(EndTimePrefs, MODE_PRIVATE)
        endTime = prefs.getLong(EndTime, 0L)
            //endTime = bundle.getLong(EndTime)
           // Log.i(TAG, "endTime : " + "${endTime}")
        diableBttnTimer()
        }


    override fun onStop() {
        super.onStop()
        timer?.cancel()
        if (endTime != null || endTime==0L) {

        }
    }


    companion object {
        const val TAG = "ForgotResetPasswordFragment"
        const val EndTime = "endTime"
        const val EndTimePrefs = "EndTimePrefs"


    }
}
