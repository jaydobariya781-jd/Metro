package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * VerifyOtpActivity — Jay Dobariya
 * 6-digit OTP verification with:
 *  - Auto-advance between OTP boxes on each digit entry
 *  - 60-second resend countdown timer
 *  - Demo OTP: "123456" always accepted
 */
class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var otpBoxes: List<EditText>
    private lateinit var btnVerify: Button
    private lateinit var tvResend: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvPhoneHint: TextView

    private var timer: CountDownTimer? = null
    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        phone = intent.getStringExtra("PHONE") ?: ""
        initViews()
        setupOtpBoxes()
        setupVerifyButton()
        startTimer()
    }

    private fun initViews() {
        tvPhoneHint = findViewById(R.id.tv_phone_number)
        tvPhoneHint.text = "+91 $phone"

        btnVerify = findViewById(R.id.btn_verify)
        tvResend = findViewById(R.id.tv_resend_label)
        tvTimer = findViewById(R.id.tv_timer)

        otpBoxes = listOf(
            findViewById(R.id.et_otp1),
            findViewById(R.id.et_otp2),
            findViewById(R.id.et_otp3),
            findViewById(R.id.et_otp4),
            findViewById(R.id.et_otp5),
            findViewById(R.id.et_otp6)
        )
    }

    private fun setupOtpBoxes() {
        for (i in otpBoxes.indices) {
            val box = otpBoxes[i]
            box.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val entered = s?.toString() ?: ""
                    if (entered.length == 1 && i < otpBoxes.lastIndex) {
                        otpBoxes[i + 1].requestFocus()
                    }
                    val otp = otpBoxes.joinToString("") { it.text.toString() }
                    btnVerify.isEnabled = otp.length == 6
                }
            })

            // Handle backspace to go to previous box
            box.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    box.text.isEmpty() && i > 0) {
                    otpBoxes[i - 1].requestFocus()
                    otpBoxes[i - 1].setText("")
                    true
                } else false
            }
        }
    }

    private fun setupVerifyButton() {
        btnVerify.setOnClickListener {
            val enteredOtp = otpBoxes.joinToString("") { it.text.toString().trim() }
            // Demo: accept "123456" or any valid 6-digit numeric OTP
            if (enteredOtp.length == 6 && enteredOtp.all { it.isDigit() }) {
                TicketManager.setLoggedIn(this, true)

                // Save user profile to Firebase Firestore
                val userName = TicketManager.getUserName(this)
                FirebaseDatabaseHelper.saveUserProfile(phone = phone, name = userName)

                Toast.makeText(this, "Verified! Welcome to Ahmedabad Metro.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                otpBoxes.forEach { it.setBackgroundResource(R.drawable.bg_otp_box_error) }
                Toast.makeText(this, "Please enter all 6 digits of your OTP.", Toast.LENGTH_SHORT).show()
            }
        }

        tvResend.setOnClickListener {
            otpBoxes.forEach { it.setText("") }
            otpBoxes[0].requestFocus()
            startTimer()
            Toast.makeText(this, "OTP resent to +91 $phone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer() {
        tvResend.isEnabled = false
        timer?.cancel()
        timer = object : CountDownTimer(60_000L, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secs = millisUntilFinished / 1000
                tvTimer.text = "00:${if (secs < 10) "0$secs" else secs}"
            }
            override fun onFinish() {
                tvTimer.text = "00:00"
                tvResend.isEnabled = true
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
