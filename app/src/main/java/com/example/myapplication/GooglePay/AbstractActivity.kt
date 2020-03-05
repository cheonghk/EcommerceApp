package com.example.myapplication.GooglePay

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData


abstract class AbstractActivity: AppCompatActivity() {
    /*
     * Handle payment result
     */
    abstract fun handleNotSuccessPaymentResult(result: PaymentResult)

    /**
     * Allow to retrieve to payment status
     *
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Manage Google Pay result
        if (requestCode == GooglePayManagement.GOOGLE_PAYMENT_CODE_RESULT) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    if (data != null) {
                        val paymentData = PaymentData.getFromIntent(data)
                        val googlePayData = paymentData!!.toJson()
                        // Execute payment
                        Payment.executeTransaction(googlePayData, this)
                    } else {
                        Payment.returnsResult(false, PaymentErrorCode.UNKNOWN_ERROR, "Unknown error", this)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    Payment.returnsResult(false, PaymentErrorCode.PAYMENT_CANCELLED_ERROR, "Payment cancelled by user", this)
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    Payment.returnsResult(false, PaymentErrorCode.UNKNOWN_ERROR, "Unknown error", this)
                }
            }
        }
    }
}