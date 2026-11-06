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
 * BookQrTicketActivity — Brij Nagvadiya
 * Full-screen QR code viewer for a specific ticket.
 * Shows the QR to scan at metro turnstiles.
 */
class BookQrTicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_qr_ticket)

        val ticketId = intent.getStringExtra("TICKET_ID") ?: ""
        val ticket = TicketManager.getTicketById(this, ticketId)

        setupHeader()

        if (ticket != null) {
            loadTicketQr(ticket)
        } else {
            Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener { finish() }
    }

    private fun loadTicketQr(ticket: TicketModel) {
        findViewById<TextView>(R.id.tv_from_station)?.text = ticket.fromStation
        findViewById<TextView>(R.id.tv_to_station)?.text = ticket.toStation
        findViewById<TextView>(R.id.tv_title)?.text = "Ticket #${ticket.ticketId}"

        // Share / pay button
        findViewById<View>(R.id.btn_pay_now)?.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Metro QR Ticket ${ticket.ticketId}")
                putExtra(Intent.EXTRA_TEXT, "Scan this QR code at Ahmedabad Metro gates for journey: ${ticket.fromStation} to ${ticket.toStation}")
            }
            startActivity(Intent.createChooser(shareIntent, "Share QR Ticket"))
        }
    }
}
