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
        findViewById<ImageView>(R.id.btn_qr_back)?.setOnClickListener { finish() }
    }

    private fun loadTicketQr(ticket: TicketModel) {
        findViewById<TextView>(R.id.tv_qr_ticket_id)?.text = ticket.ticketId
        findViewById<TextView>(R.id.tv_qr_route)?.text = "${ticket.fromStation} → ${ticket.toStation}"
        findViewById<TextView>(R.id.tv_qr_date)?.text = "${ticket.date} • ${ticket.time}"
        findViewById<TextView>(R.id.tv_qr_status)?.text = ticket.status.uppercase()

        val qrData = "METRO|${ticket.ticketId}|${ticket.fromStation}|${ticket.toStation}|${ticket.passengers}|${ticket.fare}"
        val qrBitmap: Bitmap? = QrCodeHelper.generateQrCode(qrData, 600, 600)
        qrBitmap?.let {
            findViewById<ImageView>(R.id.iv_full_qr_code)?.setImageBitmap(it)
        }

        // Share button
        findViewById<MaterialButton>(R.id.btn_share_ticket)?.setOnClickListener {
            Toast.makeText(this, "Sharing ticket ${ticket.ticketId}…", Toast.LENGTH_SHORT).show()
        }
    }
}
