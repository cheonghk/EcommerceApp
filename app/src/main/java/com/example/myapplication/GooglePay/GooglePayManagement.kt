package com.example.myapplication.GooglePay

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Google Pay Management
 */
object GooglePayManagement {

    // Technical code used for specifying the result of WebView activity
    const val GOOGLE_PAYMENT_CODE_RESULT = 985

    private val SUPPORTED_METHODS = Arrays.asList(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS"
    )

    // The name of payment processor / gateway
    // https://lyra.com/in/
    private const val PAYMENT_GATEWAY = "lyra"

    private lateinit var supportedNetworks: Array<String>


    /*
     * Define your Google Pay API version***
     */
    private fun getBaseRequest(): JSONObject {
        return JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
    }

    /**
     * Get tokenization specification
     */
    private fun getGatewayTokenizationSpecification(gatewayMerchantId: String): JSONObject {
        val params = JSONObject().put("gateway", PAYMENT_GATEWAY)
        params.put("gatewayMerchantId", gatewayMerchantId)

        return JSONObject().put("type", "PAYMENT_GATEWAY").put("parameters", params)
    }

    /**
     * Get allowed card networks
     */
    private fun getAllowedCardNetworks(): JSONArray {
        val allowedCardNetworks = JSONArray()

        for (network in supportedNetworks) {
            allowedCardNetworks.put(network)
        }

        return allowedCardNetworks
    }


    /**
     * Get allow card auth methods
     */
    private fun getAllowedCardAuthMethods(): JSONArray {
        val allowedCardAuthMethods = JSONArray()

        for (method in SUPPORTED_METHODS) {
            allowedCardAuthMethods.put(method)
        }

        return allowedCardAuthMethods
    }

    /**
     * Get card payment method
     * That uses getAllowedCardAuthMethods, getAllowedCardNetworks methods and maybe some additional parameters
     */
    private fun getBaseCardPaymentMethod(
        additionalParams: JSONObject?,
        getGatewayTokenizationSpecification: JSONObject?
    ): JSONObject {
        val params = JSONObject()
            .put("allowedAuthMethods", getAllowedCardAuthMethods())
            .put("allowedCardNetworks", getAllowedCardNetworks())
        /*.put("billingAddressRequired", true)
    val billingAddressParameters = JSONObject()
    billingAddressParameters.put("format", "FULL")
    params.put("billingAddressParameters", billingAddressParameters)*/


        // Additional parameters provided?
        if (additionalParams != null && additionalParams.length() > 0) {
            val keys = additionalParams.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                params.put(key, additionalParams.get(key))
            }
        }

        val cardPaymentMethod = JSONObject()
        cardPaymentMethod.put("type", "CARD")
        cardPaymentMethod.put(
            "parameters", params
        )

        if (getGatewayTokenizationSpecification != null) {
            cardPaymentMethod.put(
                "tokenizationSpecification", getGatewayTokenizationSpecification
            )
        }

        return cardPaymentMethod
    }

    /*
    *
    initPaymentsClient
     *
     */
    private fun createPaymentsClient(activity: Activity, mode: String): PaymentsClient {
        val builder = Wallet.WalletOptions.Builder()
        when (mode) {
            "TEST" -> builder.setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            else -> builder.setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
        }
        return Wallet.getPaymentsClient(activity, builder.build())
    }

    /**
     * Creating an IsReadyToPayRequest object to prepare ready Request
     */
    private fun getIsReadyToPayRequest(): IsReadyToPayRequest {
            val isReadyToPayRequest = getBaseRequest()
            isReadyToPayRequest.put(
                "allowedPaymentMethods", JSONArray()
                    .put(getBaseCardPaymentMethod(null, null))
            )
            return IsReadyToPayRequest.fromJson(isReadyToPayRequest.toString())
        }


    /*Prepare payment data Request by creating a PaymentDataRequest object
     */
    private fun preparePaymentDataRequest(
        price: String,
        currency: String,
        gatewayMerchantId: String
    ): PaymentDataRequest {
        val paymentDataRequestJson = getBaseRequest()
        val additionalParams = JSONObject()

        val transactionJson = JSONObject()
        transactionJson
            .put("totalPriceStatus", "FINAL")
            .put("totalPrice", price)
            .put("currencyCode", currency)

        additionalParams.put("billingAddressRequired", true)

        additionalParams
            .put(
                "billingAddressParameters", JSONObject()
                    .put("format", "FULL").put("phoneNumberRequired", false)
            )

        paymentDataRequestJson
            .put(
                "allowedPaymentMethods", JSONArray()
                    .put(
                        getBaseCardPaymentMethod(
                            additionalParams,
                            getGatewayTokenizationSpecification(gatewayMerchantId)
                        )
                    )
            )

        paymentDataRequestJson.put("shippingAddressRequired", true)
        paymentDataRequestJson.put("emailRequired", true)

        paymentDataRequestJson.put("transactionInfo", transactionJson)

        return PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
    }

    /**
     * Initializes payments client
     *
     * @param activity Activity
     * @param mode String
     * @param supportedNetworks String
     */
    internal fun init(activity: Activity, mode: String, supportedNetworks: String): PaymentsClient {
        this.supportedNetworks = supportedNetworks.split(Regex(",[ ]*")).toTypedArray()
        return createPaymentsClient(activity, mode)
    }

    /**
     * Determines if Google Pay is available
     *
     * @param paymentsClient PaymentsClient
     * @return Task<Boolean>
     */
    internal fun isPossible(paymentsClient: PaymentsClient): Task<Boolean> {
        val isReadyToPayRequest = getIsReadyToPayRequest()
        return paymentsClient.isReadyToPay(isReadyToPayRequest)
    }

    /**
     * Executes Google Pay
     *
     * @param amount String
     * @param currency String
     * @param mode String
     * @param gatewayMerchantId String
     * @param activity Activity
     * @param paymentsClient PaymentsClient
     */
    internal fun execute(
        amount: String?,
        currency: String?,
        mode: String?,
        gatewayMerchantId: String,
        activity: Activity,
        paymentsClient: PaymentsClient
    ) {
        if (amount == null) {
            Payment.returnsResult(
                false,
                PaymentErrorCode.PAYMENT_DATA_ERROR,
                "amount is required",
                activity
            )
            return
        }

        if (mode == null) {
            Payment.returnsResult(
                false,
                PaymentErrorCode.PAYMENT_DATA_ERROR,
                "mode is required",
                activity
            )
            return
        }

        if (currency == null) {
            Payment.returnsResult(
                false,
                PaymentErrorCode.PAYMENT_DATA_ERROR,
                "currency is required",
                activity
            )
            return
        }

        val paymentDataRequest = preparePaymentDataRequest(amount, currency, gatewayMerchantId)
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(paymentDataRequest),
            activity,
            GOOGLE_PAYMENT_CODE_RESULT
        )
    }
}