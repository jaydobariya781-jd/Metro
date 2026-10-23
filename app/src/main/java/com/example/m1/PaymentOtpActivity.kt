package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PaymentOtpActivity
 * Handles OTP verification for ticket bookings.
 * Features 4-digit auto-advancing input, live 30-second countdown timer,
 * full validation (empty, incomplete, expired, and incorrect code checking),
 * and dynamic visual error feedback.
 */
class PaymentOtpActivity : AppCompatActivity() {

    private var fromStation = "Old High Court"
    private var toStation = "Vastral Gam"
    private var passengers = 1
    private var journeyType = "Single Journey"
    private var date = "Today"
    private var fare = 25.0

    private lateinit var tvSubtitlePhone: TextView
    private lateinit var tvSubtitleAmount: TextView
    private lateinit var tvOtpError: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnVerifyPay: Button

    private lateinit var otpBoxes: Array<EditText>
    private var countDownTimer: CountDownTimer? = null
    private var validOtp: String = "4820"
    private var isTimerExpired: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_otp)

        fromStation = intent.getStringExtra("FROM_STATION") ?: "Old High Court"
        toStation = intent.getStringExtra("TO_STATION") ?: "Vastral Gam"
        passengers = intent.getIntExtra("PASSENGERS", 1)
        journeyType = intent.getStringExtra("JOURNEY_TYPE") ?: "Single Journey"
        date = intent.getStringExtra("DATE") ?: "Today"
        fare = intent.getDoubleExtra("FARE", 25.0)

        initViews()
        setupHeader()
        setupFareAndSubtitle()
        setupOtpInputBoxes()
        setupTimer()
        setupVerifyButton()
    }

    private fun initViews() {
        tvSubtitlePhone = findViewById(R.id.tv_subtitle_phone)
        tvSubtitleAmount = findViewById(R.id.tv_subtitle_amount)
        tvOtpError = findViewById(R.id.tv_otp_error)
        tvTimer = findViewById(R.id.tv_timer)
        btnVerifyPay = findViewById(R.id.btn_verify_pay)

        otpBoxes = arrayOf(
            findViewById(R.id.et_otp1),
            findViewById(R.id.et_otp2),
            findViewById(R.id.et_otp3),
            findViewById(R.id.et_otp4)
        )
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.tv_title)?.setOnClickListener {
            Toast.makeText(this, "256-Bit Encrypted UPI Payment Gateway", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupFareAndSubtitle() {
        val formattedFare = "₹${"%.2f".format(fare)}"
        tvSubtitleAmount.text = "of $formattedFare"
        btnVerifyPay.text = "🔒  Verify & Pay $formattedFare"

        val phone = TicketManager.getUserPhone(this).ifEmpty { "+91 98765 43210" }
        val subtitleHtml = "Sent to <b>$phone</b> to confirm payment<br/><font color='#0052CC'>Demo OTP: <b>$validOtp</b></font>"
        tvSubtitlePhone.text = Html.fromHtml(subtitleHtml, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun setupOtpInputBoxes() {
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
                            if (entered.length == 4) {
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

    private fun setupTimer() {
        startTimer(30)

        findViewById<View>(R.id.ll_resend)?.setOnClickListener {
            if (isTimerExpired || tvTimer.text.toString().contains("Resend", ignoreCase = true)) {
                resendOtp()
            }
        }
    }

    private fun resendOtp() {
        validOtp = "4820"
        isTimerExpired = false
        hideError()

        for (box in otpBoxes) {
            box.text.clear()
            box.setBackgroundResource(R.drawable.bg_otp_box_inactive)
        }
        otpBoxes[0].requestFocus()

        setupFareAndSubtitle()
        startTimer(30)

        Toast.makeText(this, "New OTP ($validOtp) sent to registered mobile!", Toast.LENGTH_SHORT).show()
    }

    private fun startTimer(seconds: Long) {
        countDownTimer?.cancel()
        isTimerExpired = false
        countDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvTimer.text = "00:${if (secondsLeft < 10) "0$secondsLeft" else secondsLeft}"
            }

            override fun onFinish() {
                isTimerExpired = true
                tvTimer.text = "Resend Now"
                showError("OTP expired. Please tap Resend Now to request a new code.")
            }
        }.start()
    }

    private fun showError(message: String) {
        tvOtpError.text = message
        tvOtpError.visibility = View.VISIBLE

        tvOtpError.animate().translationXBy(8f).setDuration(40).withEndAction {
            tvOtpError.animate().translationXBy(-16f).setDuration(40).withEndAction {
                tvOtpError.animate().translationXBy(8f).setDuration(40).start()
            }.start()
        }.start()
    }

    private fun hideError() {
        if (tvOtpError.visibility == View.VISIBLE) {
            tvOtpError.visibility = View.GONE
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
                showError("OTP has expired. Please tap 'Resend Now' to request a new code.")
                highlightBoxesError()
                return@setOnClickListener
            }

            // Read entered OTP
            val enteredOtp = otpBoxes.joinToString("") { it.text.toString().trim() }

            // Validation 2: Check empty
            if (enteredOtp.isEmpty()) {
                showError("Please enter the 4-digit OTP code.")
                highlightBoxesError()
                otpBoxes[0].requestFocus()
                return@setOnClickListener
            }

            // Validation 3: Check incomplete code length
            if (enteredOtp.length < 4) {
                showError("Incomplete code. Please enter all 4 digits.")
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

            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val currentTime = timeFormat.format(Calendar.getInstance().time)

            val dateCompact = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
            val randomSuffix = (1000..9999).random()
            val ticketId = "AMD${dateCompact}$randomSuffix"

            val qrContent = "TicketID: $ticketId\n" +
                    "From: $fromStation\n" +
                    "To: $toStation\n" +
                    "Passengers: $passengers\n" +
                    "Fare: ₹${"%.2f".format(fare)}\n" +
                    "Status: PAID"

            val ticket = TicketModel(
                ticketId = ticketId,
                fromStation = fromStation,
                toStation = toStation,
                passengers = passengers,
                ticketType = journeyType,
                date = date,
                time = currentTime,
                fare = fare,
                status = "PAID",
                paymentMethod = "UPI / OTP Verified",
                qrData = qrContent
            )

            TicketManager.saveTicket(this, ticket)

            Toast.makeText(this, "Payment Successful ✓", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, BookingSuccessActivity::class.java).apply {
                putExtra("TICKET_ID", ticketId)
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
