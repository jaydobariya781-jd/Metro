package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PaymentActivity — Brij Nagvadiya
 * Displays booking summary and payment method selection.
 * Methods: UPI (Google Pay / PhonePe / Paytm), Smart Card, Cash.
 * Proceeds to PaymentOtpActivity for UPI confirmation.
 */
class PaymentActivity : AppCompatActivity() {

    private var fromStation = ""
    private var toStation = ""
    private var passengers = 1
    private var fare = 0.0
    private var selectedMethod = "UPI (Google Pay)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        fromStation = intent.getStringExtra("FROM_STATION") ?: "Thaltej"
        toStation = intent.getStringExtra("TO_STATION") ?: "Vastral Gam"
        passengers = intent.getIntExtra("PASSENGERS", 1)
        fare = intent.getDoubleExtra("FARE", 25.0)

        setupHeader()
        populateSummary()
        setupPaymentMethods()
        setupPayNow()
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_payment_back)?.setOnClickListener { finish() }
    }

    private fun populateSummary() {
        findViewById<TextView>(R.id.tv_payment_route)?.text = "$fromStation → $toStation"
        val passengerLabel = if (passengers == 1) "1 Passenger" else "$passengers Passengers"
        findViewById<TextView>(R.id.tv_payment_passengers)?.text = passengerLabel
        findViewById<TextView>(R.id.tv_payment_fare)?.text = "₹${"%.2f".format(fare)}"
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.tv_payment_date)?.text = "$today • $time"
    }

    private fun setupPaymentMethods() {
        // UPI Methods
        setupMethodCard(R.id.card_pay_gpay, "UPI (Google Pay)")
        setupMethodCard(R.id.card_pay_phonepe, "UPI (PhonePe)")
        setupMethodCard(R.id.card_pay_paytm, "UPI (Paytm)")
        // Card / Smart Card
        setupMethodCard(R.id.card_pay_smartcard, "Metro Smart Card")
        // Cash
        setupMethodCard(R.id.card_pay_cash, "Cash")
    }

    private fun setupMethodCard(viewId: Int, method: String) {
        findViewById<CardView>(viewId)?.setOnClickListener {
            selectedMethod = method
            Toast.makeText(this, "Selected: $method", Toast.LENGTH_SHORT).show()
            // Highlight selected card
            updateSelectedMethodUI(viewId)
        }
    }

    private fun updateSelectedMethodUI(selectedId: Int) {
        val allCardIds = listOf(
            R.id.card_pay_gpay, R.id.card_pay_phonepe, R.id.card_pay_paytm,
            R.id.card_pay_smartcard, R.id.card_pay_cash
        )
        for (id in allCardIds) {
            val card = findViewById<CardView>(id)
            card?.cardElevation = if (id == selectedId) 8f else 2f
        }
    }

    private fun setupPayNow() {
        findViewById<MaterialButton>(R.id.btn_pay_now)?.setOnClickListener {
            val isUpi = selectedMethod.startsWith("UPI")
            if (isUpi) {
                // Route through OTP for UPI payments
                val intent = Intent(this, PaymentOtpActivity::class.java).apply {
                    putExtra("FROM_STATION", fromStation)
                    putExtra("TO_STATION", toStation)
                    putExtra("PASSENGERS", passengers)
                    putExtra("FARE", fare)
                    putExtra("PAYMENT_METHOD", selectedMethod)
                }
                startActivity(intent)
            } else {
                // Non-UPI: confirm booking directly
                confirmBooking()
            }
        }
    }

    private fun confirmBooking() {
        val ticketId = TicketManager.generateTicketId(this)
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val ticket = TicketModel(
            ticketId = ticketId,
            fromStation = fromStation,
            toStation = toStation,
            passengers = passengers,
            fare = fare,
            date = today,
            time = time,
            paymentMethod = selectedMethod,
            status = "Active"
        )
        TicketManager.addTicket(this, ticket)
        val intent = Intent(this, BookingSuccessActivity::class.java).apply {
            putExtra("TICKET_ID", ticketId)
        }
        startActivity(intent)
        finish()
    }
}
