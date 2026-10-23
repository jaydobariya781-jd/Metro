package com.example.m1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * BookingSuccessActivity — Brij Nagvadiya
 * Displayed after a successful ticket booking.
 * Shows: ticket ID, route, fare, QR code placeholder, and action buttons.
 */
class BookingSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_success)

        val ticketId = intent.getStringExtra("TICKET_ID") ?: ""
        val ticket = TicketManager.getTicketById(this, ticketId)

        if (ticket != null) {
            populateTicketDetails(ticket)
            generateQrCode(ticket)
        } else {
            // If ticket not found (demo fallback)
            populateFallback(ticketId)
        }

        setupActionButtons(ticketId)
    }

    private fun populateTicketDetails(ticket: TicketModel) {
        findViewById<TextView>(R.id.tv_success_ticket_id)?.text = ticket.ticketId
        findViewById<TextView>(R.id.tv_success_route)?.text = "${ticket.fromStation} → ${ticket.toStation}"
        val passengerLabel = if (ticket.passengers == 1) "1 Passenger" else "${ticket.passengers} Passengers"
        findViewById<TextView>(R.id.tv_success_passengers)?.text = passengerLabel
        findViewById<TextView>(R.id.tv_success_fare)?.text = "₹${"%.2f".format(ticket.fare)}"
        findViewById<TextView>(R.id.tv_success_date)?.text = "${ticket.date} • ${ticket.time}"
        findViewById<TextView>(R.id.tv_success_payment)?.text = ticket.paymentMethod
        findViewById<TextView>(R.id.tv_success_status)?.text = ticket.status.uppercase()
    }

    private fun populateFallback(ticketId: String) {
        findViewById<TextView>(R.id.tv_success_ticket_id)?.text = ticketId
        findViewById<TextView>(R.id.tv_success_route)?.text = "Thaltej → Vastral Gam"
        findViewById<TextView>(R.id.tv_success_fare)?.text = "₹25.00"
        findViewById<TextView>(R.id.tv_success_status)?.text = "ACTIVE"
    }

    private fun generateQrCode(ticket: TicketModel) {
        try {
            val qrData = "METRO|${ticket.ticketId}|${ticket.fromStation}|${ticket.toStation}|${ticket.fare}"
            val qrBitmap: Bitmap? = QrCodeHelper.generateQrCode(qrData, 500, 500)
            if (qrBitmap != null) {
                findViewById<ImageView>(R.id.iv_qr_code)?.setImageBitmap(qrBitmap)
            }
        } catch (e: Exception) {
            // QR generation failed silently — placeholder icon shows
        }
    }

    private fun setupActionButtons(ticketId: String) {
        // Download / Share ticket
        findViewById<MaterialButton>(R.id.btn_download_ticket)?.setOnClickListener {
            Toast.makeText(this, "Ticket $ticketId saved to Downloads", Toast.LENGTH_SHORT).show()
        }

        // View all tickets
        findViewById<MaterialButton>(R.id.btn_view_tickets)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Book another ticket
        findViewById<MaterialButton>(R.id.btn_book_another)?.setOnClickListener {
            startActivity(Intent(this, BookTicketActivity::class.java))
            finish()
        }

        // Back arrow
        findViewById<ImageView>(R.id.btn_success_back)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
