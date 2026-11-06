package com.example.m1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
        findViewById<TextView>(R.id.tv_booking_ref)?.text = "Ticket ID: ${ticket.ticketId}"
        findViewById<TextView>(R.id.tv_from_station)?.text = ticket.fromStation
        findViewById<TextView>(R.id.tv_to_station)?.text = ticket.toStation
        val passengerLabel = if (ticket.passengers == 1) "1 Passenger" else "${ticket.passengers} Passengers"
        findViewById<TextView>(R.id.tv_passengers)?.text = passengerLabel
        findViewById<TextView>(R.id.tv_amount_paid)?.text = "₹${"%.2f".format(ticket.fare)}"
        findViewById<TextView>(R.id.tv_booking_date)?.text = "${ticket.date} • ${ticket.time}"
        findViewById<TextView>(R.id.tv_passenger_name)?.text = ticket.paymentMethod
        findViewById<TextView>(R.id.tv_ticket_status_badge)?.text = ticket.status.uppercase()
    }

    private fun populateFallback(ticketId: String) {
        findViewById<TextView>(R.id.tv_booking_ref)?.text = "Ticket ID: $ticketId"
        findViewById<TextView>(R.id.tv_from_station)?.text = "Thaltej"
        findViewById<TextView>(R.id.tv_to_station)?.text = "Vastral Gam"
        findViewById<TextView>(R.id.tv_amount_paid)?.text = "₹25.00"
        findViewById<TextView>(R.id.tv_ticket_status_badge)?.text = "ACTIVE"
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
        findViewById<View>(R.id.btn_save)?.apply {
            setOnClickListener {
                Toast.makeText(this@BookingSuccessActivity, "Ticket $ticketId saved to device storage", Toast.LENGTH_SHORT).show()
            }
            setOnLongClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Ahmedabad Metro Ticket $ticketId")
                    putExtra(Intent.EXTRA_TEXT, "Ahmedabad Metro Digital QR Ticket: $ticketId. Keep your QR ready at entry turnstiles.")
                }
                startActivity(Intent.createChooser(shareIntent, "Share Metro Ticket"))
                true
            }
        }

        // View all tickets
        findViewById<View>(R.id.btn_view_my_tickets)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Return to home
        findViewById<View>(R.id.btn_go_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
