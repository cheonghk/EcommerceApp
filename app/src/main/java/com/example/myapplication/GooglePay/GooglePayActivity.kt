/*package com.example.myapplication.GooglePay

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import jdk.nashorn.internal.runtime.ECMAException.getException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.experimental.results.ResultMatchers.isSuccessful
import java.util.*


class GooglePayActivity :AppCompatActivity(){
    lateinit var mPaymentsClient : PaymentsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val walletOptions = Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()

        mPaymentsClient = Wallet.getPaymentsClient(this, walletOptions)


    }


    private fun possiblyShowGooglePayButton() {
        val isReadyToPayJson: Optional<JSONObject> = PaymentsUtil.getIsReadyToPayRequest()
        if (!isReadyToPayJson.isPresent) {
            return
        }
        val request =
            IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString())
                ?: return
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
// OnCompleteListener to be triggered when the result of the call is known.
        val task: Task<Boolean> =
            mPaymentsClient.isReadyToPay(request)
        task.addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                setGooglePayAvailable(task.result)
            } else {
                Log.w("isReadyToPay failed", task.exception)
            }
        }
    }


    companion object {
        @Throws(JSONException::class)
        private fun getBaseRequest(): JSONObject? {
            return JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0)
        }

        @Throws(JSONException::class)
        private fun getGatewayTokenizationSpecification(): JSONObject? {
            return object : JSONObject() {
                init {
                    put("type", "PAYMENT_GATEWAY")
                    put("parameters", object : JSONObject() {
                        init {
                            put("gateway", "example")
                            put("gatewayMerchantId", "exampleGatewayMerchantId")
                        }
                    })
                }
            }
        }

        private fun  getAllowedCardNetworks() : JSONArray? {
            return JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("INTERAC")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA");
        }

        private fun getAllowedCardAuthMethods(): JSONArray? {
            return JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS")
        }


        @Throws(JSONException::class)
        private fun getBaseCardPaymentMethod(): JSONObject? {
            val cardPaymentMethod = JSONObject()
            cardPaymentMethod.put("type", "CARD")
            val parameters = JSONObject()
            parameters.put("allowedAuthMethods", getAllowedCardAuthMethods())
            parameters.put("allowedCardNetworks", getAllowedCardNetworks())
            // Optionally, you can add billing address/phone number associated with a CARD payment method.
            parameters.put("billingAddressRequired", true)
            val billingAddressParameters = JSONObject()
            billingAddressParameters.put("format", "FULL")
            parameters.put("billingAddressParameters", billingAddressParameters)
            cardPaymentMethod.put("parameters", parameters)
            return cardPaymentMethod
        }


        @Throws(JSONException::class)
        private fun getCardPaymentMethod(): JSONObject? {
            val cardPaymentMethod = getBaseCardPaymentMethod()
            cardPaymentMethod!!.put(
                "tokenizationSpecification",
                getGatewayTokenizationSpecification()
            )
            return cardPaymentMethod
        }


        fun getIsReadyToPayRequest(): Optional<JSONObject?>? {
            return try {
                val isReadyToPayRequest = getBaseRequest()
                isReadyToPayRequest!!.put(
                    "allowedPaymentMethods", JSONArray().put(getBaseCardPaymentMethod())
                )
                Optional.of(isReadyToPayRequest)
            } catch (e: JSONException) {
                Optional.empty()
            }
        }
    }
}*/