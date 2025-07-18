package com.rajat.razorpaygateway

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PayloadHelper
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class MainActivity : AppCompatActivity(), PaymentResultWithDataListener, ExternalWalletListener {
    lateinit var payBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        payBtn = findViewById(R.id.payBtn)

        payBtn.setOnClickListener {
            startPayment()
        }
    }
    private fun startPayment() {
        Checkout.preload(this)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("user-your-key")

        try {
            val options = JSONObject()
            options.put("name","Rajat")
//            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image",R.drawable.ic_launcher_background)
            options.put("theme.color", "#3399cc")
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb")
            options.put("amount","1000")//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email","rajat@gmail.com")
            prefill.put("contact","1234567890")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(stringData: String?, p1: PaymentData?) {
    println("Check Payment Successfully or not: $stringData")
        println("Check Payment Data: $p1")
        Toast.makeText(this, "Payment is successful : " + stringData, Toast.LENGTH_SHORT).show();
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        println("Check Payment Data: $p1")
        Toast.makeText(this, "Payment Failed due to error : " + p1, Toast.LENGTH_SHORT).show();
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {

    }
}