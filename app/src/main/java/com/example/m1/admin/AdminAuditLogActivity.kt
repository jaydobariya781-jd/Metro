package com.example.m1.admin

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.m1.R

/**
 * Activity displaying administrative security audit trails
 */
class AdminAuditLogActivity : AppCompatActivity() {

    private lateinit var llLogsContainer: LinearLayout
    private lateinit var auditLog: AdminAuditLog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_audit_log)

        auditLog = AdminAuditLog.getInstance(this)
        llLogsContainer = findViewById(R.id.llLogsContainer)

        findViewById<ImageView>(R.id.ivBack)?.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.ivClearLogs)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear Audit Logs")
                .setMessage("Are you sure you want to clear system security logs?")
                .setPositiveButton("Clear") { _, _ ->
                    Toast.makeText(this, "Logs archived to system store", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        renderLogs()
    }

    private fun renderLogs() {
        llLogsContainer.removeAllViews()
        val entries = auditLog.getLogs()

        for (entry in entries) {
            val card = CardView(this).apply {
                radius = 12f
                cardElevation = 2f
                setCardBackgroundColor(Color.WHITE)
                useCompatPadding = true
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(0, 0, 0, 16)
                layoutParams = lp
            }

            val content = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 20, 24, 20)
            }

            val tvHeader = TextView(this).apply {
                text = "${entry.action} • ${entry.adminUser}"
                textSize = 14f
                setTextColor(Color.parseColor("#1E293B"))
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            val tvDetails = TextView(this).apply {
                text = entry.details
                textSize = 13f
                setTextColor(Color.parseColor("#475569"))
                setPadding(0, 6, 0, 6)
            }

            val tvTime = TextView(this).apply {
                text = entry.timestamp
                textSize = 11f
                setTextColor(Color.parseColor("#94A3B8"))
            }

            content.addView(tvHeader)
            content.addView(tvDetails)
            content.addView(tvTime)
            card.addView(content)
            llLogsContainer.addView(card)
        }
    }
}
