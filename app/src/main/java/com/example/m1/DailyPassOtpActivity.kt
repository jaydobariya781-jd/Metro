package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Daily Pass OTP Verification Activity
 * Matches the requested Daily Pass OTP design with 6-digit auto-advancing input boxes,
 * live 45-second resend countdown timer, Verify & Pay ₹200.00 action,
 * Terms & Privacy links, and bottom navigation.
 */
class DailyPassOtpActivity : AppCompatActivity() {

    private lateinit var tvOtpSubtitle: TextView
    private lateinit var tvResendOtp: TextView
    private lateinit var tvOtpTimer: TextView
    private lateinit var btnVerifyPay: Button
    private lateinit var tvLegalFootnote: TextView
    private lateinit var tvOtpError: TextView

    private lateinit var otpBoxes: Array<EditText>
    private var countDownTimer: CountDownTimer? = null
    private var validOtp: String = "614920"
    private var isTimerExpired: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_pass_otp)

        initViews()
        setupHeader()
        setupSubtitle()
        setupOtpInputBoxes()
        setupCountdownTimer()
        setupVerifyButton()
        setupLegalFootnote()
        setupBottomNav()
    }

    private fun initViews() {
        tvOtpSubtitle = findViewById(R.id.tv_otp_subtitle)
        tvResendOtp = findViewById(R.id.tv_resend_otp)
        tvOtpTimer = findViewById(R.id.tv_otp_timer)
        btnVerifyPay = findViewById(R.id.btn_verify_pay_pass)
        tvLegalFootnote = findViewById(R.id.tv_legal_footnote)
        tvOtpError = findViewById(R.id.tv_otp_error)

        otpBoxes = arrayOf(
            findViewById(R.id.et_otp_1),
            findViewById(R.id.et_otp_2),
            findViewById(R.id.et_otp_3),
            findViewById(R.id.et_otp_4),
            findViewById(R.id.et_otp_5),
            findViewById(R.id.et_otp_6)
        )
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_daily_pass_back)?.setOnClickListener {
            finish()
        }

        // Title click
        findViewById<TextView>(R.id.tv_daily_pass_title)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro • 24-Hour Unlimited Rides Pass (₹200.00)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSubtitle() {
        val phone = TicketManager.getUserPhone(this).ifEmpty { "+91 98765 43210" }
        val maskedPhone = if (phone.length >= 10) {
            val start = phone.take(6)
            val end = phone.takeLast(3)
            "$start*** **$end"
        } else {
            "+91 98*** **123"
        }

        val htmlText = "A 6-digit code has been sent to <b>$maskedPhone</b> for your Daily Pass payment of <b>₹200.00</b>.<br/><font color='#0057B7'>Demo OTP: <b>$validOtp</b></font>"
        tvOtpSubtitle.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun setupOtpInputBoxes() {
        // Start with first box focused
        otpBoxes[0].requestFocus()

        for (i in otpBoxes.indices) {
            val currentBox = otpBoxes[i]

            currentBox.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    hideError()
                    if (!s.isNullOrEmpty()) {
                        currentBox.setBackgroundResource(R.drawable.bg_otp_box_active)
                        if (i < otpBoxes.size - 1) {
                            otpBoxes[i + 1].requestFocus()
                        } else if (i == otpBoxes.size - 1) {
                            val entered = otpBoxes.joinToString("") { it.text.toString().trim() }
                            if (entered.length == 6) {
                                btnVerifyPay.performClick()
                            }
                        }
                    } else {
                        currentBox.setBackgroundResource(R.drawable.bg_otp_box_inactive)
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            currentBox.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideError()
                    currentBox.setBackgroundResource(R.drawable.bg_otp_box_active)
                } else if (currentBox.text.isNullOrEmpty()) {
                    currentBox.setBackgroundResource(R.drawable.bg_otp_box_inactive)
                }
            }

            currentBox.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    hideError()
                    if (currentBox.text.isEmpty() && i > 0) {
                        otpBoxes[i - 1].text.clear()
                        otpBoxes[i - 1].requestFocus()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    private fun setupCountdownTimer() {
        startTimer(45)

        tvResendOtp.setOnClickListener {
            // Generate a fresh demo OTP
            validOtp = "614920"
            isTimerExpired = false
            hideError()

            for (box in otpBoxes) {
                box.text.clear()
                box.setBackgroundResource(R.drawable.bg_otp_box_inactive)
            }
            otpBoxes[0].requestFocus()

            setupSubtitle()
            startTimer(45)

            Toast.makeText(this, "New OTP ($validOtp) sent to your registered mobile!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(seconds: Long) {
        countDownTimer?.cancel()
        isTimerExpired = false
        countDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                tvOtpTimer.text = "00:${if (sec < 10) "0$sec" else sec}"
            }

            override fun onFinish() {
                tvOtpTimer.text = "00:00"
                isTimerExpired = true
                showError("OTP expired. Please tap Resend OTP to request a new code.")
            }
        }.start()
    }

    private fun showError(message: String) {
        tvOtpError.text = message
        tvOtpError.visibility = android.view.View.VISIBLE

        // Shake animation for attention
        tvOtpError.animate().translationXBy(8f).setDuration(40).withEndAction {
            tvOtpError.animate().translationXBy(-16f).setDuration(40).withEndAction {
                tvOtpError.animate().translationXBy(8f).setDuration(40).start()
            }.start()
        }.start()
    }

    private fun hideError() {
        if (tvOtpError.visibility == android.view.View.VISIBLE) {
            tvOtpError.visibility = android.view.View.GONE
        }
    }

    private fun highlightBoxesError() {
        for (box in otpBoxes) {
            box.setBackgroundResource(R.drawable.bg_otp_box_error)
        }
    }

    private fun setupVerifyButton() {
        btnVerifyPay.setOnClickListener {
            // Validation 1: Check timer expiry
            if (isTimerExpired) {
                showError("OTP has expired. Please tap 'Resend OTP' to request a new code.")
                highlightBoxesError()
                return@setOnClickListener
            }

            // Read entered OTP
            val enteredOtp = otpBoxes.joinToString("") { it.text.toString().trim() }

            // Validation 2: Check empty
            if (enteredOtp.isEmpty()) {
                showError("Please enter the 6-digit OTP code.")
                highlightBoxesError()
                otpBoxes[0].requestFocus()
                return@setOnClickListener
            }

            // Validation 3: Check incomplete code length
            if (enteredOtp.length < 6) {
                showError("Incomplete code. Please enter all 6 digits.")
                for (box in otpBoxes) {
                    if (box.text.isNullOrEmpty()) {
                        box.setBackgroundResource(R.drawable.bg_otp_box_error)
                    } else {
                        box.setBackgroundResource(R.drawable.bg_otp_box_active)
                    }
                }
                otpBoxes.firstOrNull { it.text.isNullOrEmpty() }?.requestFocus()
                return@setOnClickListener
            }

            // Validation 4: Check correctness against valid OTP
            if (enteredOtp != validOtp) {
                showError("Incorrect OTP entered. Please try again (Demo: $validOtp).")
                highlightBoxesError()
                otpBoxes[0].requestFocus()
                return@setOnClickListener
            }

            // Validation Passed
            hideError()

            // Create Daily Pass Ticket
            val dateCompact = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTime = timeFormat.format(Calendar.getInstance().time)
            val randomSuffix = (1000..9999).random()
            val passId = "AMD-PASS-${dateCompact}$randomSuffix"

            val qrContent = "PassID: $passId\n" +
                    "Type: Unlimited Daily Pass\n" +
                    "Corridor: All Lines (East-West & North-South)\n" +
                    "Valid: Today until 11:00 PM\n" +
                    "Fare: ₹200.00\n" +
                    "Status: PAID & ACTIVE"

            val passTicket = TicketModel(
                ticketId = passId,
                fromStation = "All Metro Stations (Unlimited)",
                toStation = "All Corridors & Interchanges",
                passengers = 1,
                ticketType = "Unlimited Daily Pass",
                date = "Today",
                time = currentTime,
                fare = 200.0,
                status = "PAID",
                paymentMethod = "UPI Verified",
                qrData = qrContent
            )

            TicketManager.saveTicket(this, passTicket)

            Toast.makeText(this, "Payment Successful ✓ Daily Pass Activated!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, BookingSuccessActivity::class.java).apply {
                putExtra("TICKET_ID", passId)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupLegalFootnote() {
        tvLegalFootnote.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Terms of Service & Privacy Policy")
                .setMessage(
                    "• Daily Pass provides unlimited rides across Ahmedabad Metro North-South and East-West corridors.\n\n" +
                    "• Pass is valid strictly on the date of purchase until the end of metro operations (11:00 PM).\n\n" +
                    "• The QR pass must be scanned at both entrance and exit automated fare turnstiles.\n\n" +
                    "• Non-transferable and non-refundable once activated."
                )
                .setPositiveButton("I Agree", null)
                .show()
        }
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_pass_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Tickets
        findViewById<LinearLayout>(R.id.nav_pass_tickets)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("TARGET_TAB", "TICKETS")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 3: Map
        findViewById<LinearLayout>(R.id.nav_pass_map)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("TARGET_TAB", "MAP")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 4: Profile
        findViewById<LinearLayout>(R.id.nav_pass_profile)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("TARGET_TAB", "PROFILE")
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
