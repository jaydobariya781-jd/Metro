package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PaymentActivity — Brij Nagvadiya
 * Displays booking summary and payment method selection.
 * Proceeds to PaymentOtpActivity for UPI confirmation.
 */
class PaymentActivity : AppCompatActivity() {

    private var fromStation = ""
    private var toStation = ""
    private var passengers = 1
    private var fare = 0.0
    private var selectedMethod = "UPI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        fromStation = intent.getStringExtra("FROM_STATION") ?: "Thaltej"
        toStation = intent.getStringExtra("TO_STATION") ?: "Vastral Gam"
        passengers = intent.getIntExtra("PASSENGERS", 1)
        fare = intent.getDoubleExtra("FARE", 25.0)

        setupHeader()
        populateSummary()
        setupPayNow()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_payment_back)?.setOnClickListener { finish() }
    }

    private fun populateSummary() {
        findViewById<TextView>(R.id.tv_summary_route)?.text = "$fromStation → $toStation"
        val passengerLabel = if (passengers == 1) "1 Passenger" else "$passengers Passengers"
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.tv_summary_details)?.text = "$passengerLabel • Single Journey • $today"
        findViewById<TextView>(R.id.tv_summary_fare)?.text = "₹${"%.2f".format(fare)}"
        findViewById<TextView>(R.id.tv_summary_total)?.text = "₹${"%.2f".format(fare)}"
        findViewById<TextView>(R.id.tv_bar_payment_amount)?.text = "₹${"%.2f".format(fare)}"
    }

    private fun setupPayNow() {
        findViewById<Button>(R.id.btn_pay_now)?.setOnClickListener {
            val intent = Intent(this, PaymentOtpActivity::class.java).apply {
                putExtra("FROM_STATION", fromStation)
                putExtra("TO_STATION", toStation)
                putExtra("PASSENGERS", passengers)
                putExtra("FARE", fare)
                putExtra("PAYMENT_METHOD", selectedMethod)
            }
            startActivity(intent)
        }
    }
}
