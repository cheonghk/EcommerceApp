package com.example.myapplication.ShoppingCart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.GooglePay.AbstractActivity
import com.example.myapplication.GooglePay.Payment
import com.example.myapplication.GooglePay.PaymentData
import com.example.myapplication.GooglePay.PaymentResult
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.Utils.FireStoreRetrivalUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.PaymentsClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.shoppingcart.*
import java.util.*


// Merchant server url
// FIXME: change by the right payment server
private const val SERVER_URL = "<REPLACE_ME>"

// Gateway merchant ID
// FIXME: change by the right identifier
private const val GATEWAY_MERCHANT_ID = "<REPLACE_ME>"

// One or more card networks you support also supported by the Google Pay API
// FIXME: change by what you can supported
private const val SUPPORTED_NETWORKS = "AMEX, VISA, MASTERCARD, DISCOVER, JCB"

// Environment TEST or PRODUCTION, refer to documentation for more information
// FIXME: change by your targeted environment
private const val TEST_PAYMENT_MODE = "TEST"



/**
 * Main activity
 *
 * This main activity allows to user to fill payment data (amount, order id, so on)
 * Before retrieving these payment data:
 * <li>Initialize payment context with LyraPayment.init(activity: Activity, mode: String, supportedNetworks: String) method</li>
 * <li>Check payment possibility with LyraPayment.isPaymentPossible(paymentsClient: PaymentsClient) method</li>
 * After retrieving these payment data:
 * <li>LyraPayment.execute(payload: JSONObject, serverUrl: String, gatewayMerchantId: String, activity: Activity, paymentsClient: PaymentsClient) is executed</li>.
 * <li>The payment result is handled by handlePaymentResult(result: PaymentResult) method</li>
 *
 * For readability purposes in this example, we do not use logs
 * @author Lyra Network
 */

class ShoppingCartActivity: AbstractActivity()  {

    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val mFireBaseCollector = FireBaseCollector()
    private var adapter :RecyclerviewShoppingCartAdapter? = null
    private var updateAllTotalAmount = 0.0
    private var user : FirebaseUser? = null
    private var email : String? = null
    private lateinit var paymentsClient: PaymentsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppingcart)
        mAuth = FirebaseAuth.getInstance()
        recyclerview_shoppingcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        // Environment TEST or PRODUCTION
        paymentsClient = Payment.init(this, TEST_PAYMENT_MODE, SUPPORTED_NETWORKS)

        Payment.isPaymentPossible(paymentsClient).addOnCompleteListener { task ->
            try {
                val result = task.getResult(ApiException::class.java)
                if (result!!) {
                    // show Google Pay as a payment option
                    //payBtn.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "isPaymentPossible return false", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "isPaymentPossible exception catched", Toast.LENGTH_LONG).show()
            }

        }
        }

    override fun onStart() {
        super.onStart()
        initUserStatus()
    }

    fun initUserStatus(){
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
            updateUI(true)
        }
    }

    fun updateUI(isInitilize :Boolean) {
        var userShoppingCartList = mutableListOf<ShoppingCartModel>()
        if (user != null) {   // Name, email address, and profile photo Url
            // val name = user.displayName
            val email = user?.email
            // val photoUrl = user.photoUrl
            // Check if user's email is verified
            //val emailVerified = user.isEmailVerified
            val uid = user?.uid

            this.email = email

            FireStoreRetrivalUtils.mFirebaseFirestore(uid!!)
                .get().addOnSuccessListener { userItems ->
                    if (!userItems!!.isEmpty) {
                        val userItemList = userItems.documents
                        //userShoppingCartList.clear() //prevent repeatedly loading items when activity start
                        for (i in userItemList) {
                            val cartItems = i.toObject(ShoppingCartModel::class.java)
                            userShoppingCartList.add(cartItems!!)}
                        if(isInitilize) {
                            adapter = RecyclerviewShoppingCartAdapter(userShoppingCartList, uid)

                            recyclerview_shoppingcart.adapter = adapter
                            recyclerview_shoppingcart.visibility = View.VISIBLE
                        }else{
                           adapter!!.setData(userShoppingCartList)
                        }

                        initTotalAmount(userShoppingCartList)
                        callBackFromAdapter()

                    }else{
                        initTotalAmount(userShoppingCartList)
                        textview_cartempty.visibility = View.VISIBLE
                        recyclerview_shoppingcart.visibility = View.INVISIBLE
                    }
                }.addOnFailureListener {
                    Log.i(TAG, " updateUI fail")
                }
        }}

    fun initTotalAmount(userShoppingCartList : MutableList<ShoppingCartModel>){
        var allTotalAmount : Double = 0.0
        var num : Int
        var price :Long

        if(userShoppingCartList.size==0){ //no item, empty cart
            totalAmountText.text = "$" + allTotalAmount.toString()
            checkOutBttnIsClickable(false)
            return
        }

        for(position in 0 until userShoppingCartList.size) {
            var category = userShoppingCartList.get(position).category
            var subcategory_position = userShoppingCartList.get(position).sub_category
            var totalItems = userShoppingCartList.get(position).totalItems
            //var unicode = userShoppingCartList.get(position).unicode

            mFireBaseCollector.readData_userShoppingCart(object :
                FireBaseCollector.ShoppingCartDataStatus {
                override fun ShoppingCartData(retriveListByCategoryPosition: MutableList<MutableList<ItemInfo_Firebase_Model>>) {
                    //retrivedListByCategoryPosition.add(retriveListByCategoryPosition)
                    fun dataProvider(): ItemInfo_Firebase_Model {
                        return retriveListByCategoryPosition.get(category!! - 1)
                            .get(subcategory_position!!)
                    }

                    price = dataProvider().price!!
                    num = totalItems!!
                    val singleItemTotalPrice = price * num
                    allTotalAmount += singleItemTotalPrice
                    if (position == userShoppingCartList.size-1)
                        totalAmountText.text = "$" + allTotalAmount.toString()
                    updateAllTotalAmount = allTotalAmount
                }
            })
        }
        checkOutBttnIsClickable(true)
        }

    fun callBackFromAdapter(){
        adapter!!.setCallBack(object : RecyclerviewShoppingCartAdapter.CallBack{

            override fun updateTotalAmount(updatePrice : Double) {
                updateAllTotalAmount += updatePrice
                val updatedAmount = updateAllTotalAmount
                totalAmountText.text = "$" + updatedAmount.toString()
            }

            override fun updateUIAfterDeletedItem() {
                updateUI(false)
                Toast.makeText(this@ShoppingCartActivity, "Item is deleted", Toast.LENGTH_SHORT).show()
            }
        })}


    override fun onResume() {
        super.onResume()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener!!)
        }
    }

    fun checkOutBttnIsClickable(canbecheckout : Boolean){
        if(canbecheckout){
            checkoutBttn.isClickable = true
            checkoutBttn.alpha = 1f
        }
        else{checkoutBttn.alpha = 0.3f
            checkoutBttn.isClickable = false}
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener {}
    }


    //**************payment methods************


    fun onPayClick(view: View) {
      //  progressBar.visibility = View.VISIBLE

        Payment.execute(createPaymentPayload(), SERVER_URL, GATEWAY_MERCHANT_ID, this, paymentsClient)
    }

    /**
     * Create PaymentData object used as payload of HTTP request to Merchant server
     *
     * @return PaymentData
     */
    private fun createPaymentPayload(): PaymentData {
        val paymentData = PaymentData()
        paymentData.setOrderId(random())
        paymentData.setAmount(updateAllTotalAmount.toString())
        paymentData.setEmail(email)

        // HK's currency code
        paymentData.setCurrency("344")

        return paymentData
    }

    override fun handlePaymentResult(result: PaymentResult) {
        //progressBar.visibility = View.GONE
        if (result.isSuccess()) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this,
                "Payment failed. errorCode = " + result.getErrorCode() + " and cause = " + result.getCause(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //************payment methods****************

    companion object {const val TAG = "ShoppingCartActivity"
        fun random(): String? {
            val generator = Random()
            return (generator.nextInt(20) + 32).toString()
        }
}}