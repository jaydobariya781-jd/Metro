package com.example.m1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

/**
 * Help & Support Activity
 * Displays comprehensive support topics, FAQs, live chat, helpline contacts,
 * bug reporting, user feedback, legal agreements, and search filtering.
 */
class HelpSupportActivity : AppCompatActivity() {

    private lateinit var etSearchHelp: EditText
    private lateinit var cardFaqs: CardView
    private lateinit var cardChat: CardView
    private lateinit var cardMobile: CardView
    private lateinit var cardTech: CardView
    private lateinit var cardFeedback: CardView
    private lateinit var cardPrivacy: CardView
    private lateinit var cardTerms: CardView

    private lateinit var tvHeaderFaq: TextView
    private lateinit var tvHeaderContact: TextView
    private lateinit var tvHeaderReporting: TextView
    private lateinit var tvHeaderLegal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)

        initViews()
        setupHeader()
        setupSearch()
        setupHelpCards()
        setupBottomNav()
    }

    private fun initViews() {
        etSearchHelp = findViewById(R.id.et_search_help)
        cardFaqs = findViewById(R.id.card_general_faqs)
        cardChat = findViewById(R.id.card_chat_support)
        cardMobile = findViewById(R.id.card_mobile_contact)
        cardTech = findViewById(R.id.card_technical_problems)
        cardFeedback = findViewById(R.id.card_send_feedback)
        cardPrivacy = findViewById(R.id.card_privacy_policy)
        cardTerms = findViewById(R.id.card_terms_of_service)

        tvHeaderFaq = findViewById(R.id.tv_header_faq)
        tvHeaderContact = findViewById(R.id.tv_header_contact)
        tvHeaderReporting = findViewById(R.id.tv_header_reporting)
        tvHeaderLegal = findViewById(R.id.tv_header_legal)
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_help_back)?.setOnClickListener {
            finish()
        }

        // Title click
        findViewById<TextView>(R.id.tv_help_title)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro Help & Support Center", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSearch() {
        etSearchHelp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                filterTopics(s?.toString()?.trim() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterTopics(query: String) {
        val q = query.lowercase()

        val matchFaq = "general faqs".contains(q) || "quick answers to common questions".contains(q)
        cardFaqs.visibility = if (matchFaq || q.isEmpty()) View.VISIBLE else View.GONE

        val matchChat = "chat with support".contains(q) || "typical response time: 5 mins".contains(q)
        cardChat.visibility = if (matchChat || q.isEmpty()) View.VISIBLE else View.GONE

        val matchMobile = "mobile no us".contains(q) || "+91 95214 01455".contains(q)
        cardMobile.visibility = if (matchMobile || q.isEmpty()) View.VISIBLE else View.GONE

        val matchTech = "technical problems".contains(q) || "app bugs or performance issues".contains(q)
        cardTech.visibility = if (matchTech || q.isEmpty()) View.VISIBLE else View.GONE

        val matchFeedback = "send feedback".contains(q) || "help us improve our service".contains(q)
        cardFeedback.visibility = if (matchFeedback || q.isEmpty()) View.VISIBLE else View.GONE

        val matchPrivacy = "privacy policy".contains(q) || "how we handle your data".contains(q)
        cardPrivacy.visibility = if (matchPrivacy || q.isEmpty()) View.VISIBLE else View.GONE

        val matchTerms = "terms of service".contains(q) || "usage rules and agreements".contains(q)
        cardTerms.visibility = if (matchTerms || q.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupHelpCards() {
        // 1. General FAQs
        cardFaqs.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Frequently Asked Questions")
                .setMessage(
                    "1. How do I purchase a Metro QR ticket?\n" +
                    "Select your origin and destination station in 'Book Ticket', choose single or return journey, and complete payment via UPI/Card. Your QR code generates immediately.\n\n" +
                    "2. How long is my QR ticket valid?\n" +
                    "Single journey tickets are valid for 120 minutes from the time of issue.\n\n" +
                    "3. What are the Metro operating timings?\n" +
                    "Daily from 06:00 AM to 11:00 PM with trains every 5-10 minutes during peak hours.\n\n" +
                    "4. What if turnstile gate doesn't scan my phone?\n" +
                    "Ensure your screen brightness is at maximum and hold the QR code 10cm above the scanner."
                )
                .setPositiveButton("Got It", null)
                .show()
        }

        // 2. Chat with Support
        cardChat.setOnClickListener {
            showLiveChatDialog()
        }

        // 3. Mobile No Us (+91 95214 01455)
        cardMobile.setOnClickListener {
            val phoneNum = "+919521401455"
            val options = arrayOf(
                "Call Now (+91 95214 01455)",
                "Copy Phone Number",
                "Send SMS"
            )

            AlertDialog.Builder(this)
                .setTitle("Contact Support")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phoneNum")
                            }
                            startActivity(intent)
                        }
                        1 -> {
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Metro Helpline", phoneNum)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(this, "Phone number copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                        2 -> {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("sms:$phoneNum")
                                putExtra("sms_body", "Hello Ahmedabad Metro Support, I need assistance with: ")
                            }
                            startActivity(intent)
                        }
                    }
                }
                .show()
        }

        // 4. Technical Problems
        cardTech.setOnClickListener {
            showTechnicalBugDialog()
        }

        // 5. Send Feedback
        cardFeedback.setOnClickListener {
            showFeedbackDialog()
        }

        // 6. Privacy Policy
        cardPrivacy.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage(
                    "Ahmedabad Metro Rail Corporation Limited (GMRCL) Data Policy:\n\n" +
                    "• Data Collection: We collect phone numbers and names strictly for authentication and ticket generation.\n\n" +
                    "• Location Data: Used exclusively to determine your nearest metro station and calculate route duration.\n\n" +
                    "• Payment Security: All payments adhere to RBI and PCI-DSS compliance. We never store credit/debit card numbers or UPI PINs.\n\n" +
                    "• Transparency: You can request account deletion or export your travel logs at any time."
                )
                .setPositiveButton("Close", null)
                .show()
        }

        // 7. Terms of Service
        cardTerms.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Terms of Service")
                .setMessage(
                    "Metro Railways (Operations and Maintenance) Act:\n\n" +
                    "• Traveling without a valid QR ticket or Smart Card attracts a statutory fine of ₹500 plus maximum fare.\n\n" +
                    "• Passengers must tap out at exit turnstiles within 120 minutes of entry.\n\n" +
                    "• Inflammable goods, weapons, and oversized baggage exceeding 25kg are strictly prohibited.\n\n" +
                    "• Reserved seats for elderly passengers and ladies must be respected."
                )
                .setPositiveButton("I Understand", null)
                .show()
        }
    }

    private fun showLiveChatDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val tvStatus = TextView(this).apply {
            text = "🟢 Support Agent Online • Typical reply: 2 mins"
            setTextColor(android.graphics.Color.parseColor("#16A34A"))
            textSize = 12f
            setPadding(0, 0, 0, 15)
        }

        val etMessage = EditText(this).apply {
            hint = "Type your query here..."
            minLines = 3
        }

        layout.addView(tvStatus)
        layout.addView(etMessage)

        AlertDialog.Builder(this)
            .setTitle("Chat with Metro Support")
            .setView(layout)
            .setPositiveButton("Send Message") { _, _ ->
                val msg = etMessage.text.toString().trim()
                if (msg.isNotEmpty()) {
                    val ticketRef = "TKT-${System.currentTimeMillis() % 100000}"
                    Toast.makeText(this, "Message sent! Support ticket #$ticketRef created. Agent will respond shortly.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Please enter a message to send", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTechnicalBugDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val categories = arrayOf(
            "QR Ticket Scan Failed at Gate",
            "Payment Debited but Ticket not Generated",
            "App Crashing or Freezing",
            "Station Schedule not Updating",
            "Other Issue"
        )

        val spinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@HelpSupportActivity, android.R.layout.simple_spinner_dropdown_item, categories)
        }

        val etDesc = EditText(this).apply {
            hint = "Describe what happened..."
            minLines = 3
            setPadding(10, 20, 10, 10)
        }

        layout.addView(TextView(this).apply { text = "Select Problem Category:"; setPadding(0, 5, 0, 8) })
        layout.addView(spinner)
        layout.addView(TextView(this).apply { text = "Details:"; setPadding(0, 15, 0, 8) })
        layout.addView(etDesc)

        AlertDialog.Builder(this)
            .setTitle("Report Technical Problem")
            .setView(layout)
            .setPositiveButton("Submit Report") { _, _ ->
                val refId = "TKT-${System.currentTimeMillis() % 100000}"
                Toast.makeText(this, "Report submitted! Ticket ID: #$refId. Our engineers are investigating.", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showFeedbackDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val ratings = arrayOf("⭐⭐⭐⭐⭐ (Excellent)", "⭐⭐⭐⭐ (Good)", "⭐⭐⭐ (Average)", "⭐⭐ (Needs Improvement)", "⭐ (Poor)")
        val spinnerRating = Spinner(this).apply {
            adapter = ArrayAdapter(this@HelpSupportActivity, android.R.layout.simple_spinner_dropdown_item, ratings)
        }

        val etComments = EditText(this).apply {
            hint = "Tell us what you liked or how we can improve..."
            minLines = 3
            setPadding(10, 20, 10, 10)
        }

        layout.addView(TextView(this).apply { text = "How would you rate your app experience?"; setPadding(0, 5, 0, 8) })
        layout.addView(spinnerRating)
        layout.addView(TextView(this).apply { text = "Your Feedback:"; setPadding(0, 15, 0, 8) })
        layout.addView(etComments)

        AlertDialog.Builder(this)
            .setTitle("Send Feedback")
            .setView(layout)
            .setPositiveButton("Submit Feedback") { _, _ ->
                Toast.makeText(this, "Thank you for your valuable feedback! We appreciate your support.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_user_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Tickets
        findViewById<LinearLayout>(R.id.nav_user_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }

        // Tab 3: Map
        findViewById<LinearLayout>(R.id.nav_user_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }

        // Tab 4: Profile
        findViewById<LinearLayout>(R.id.nav_user_profile)?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
