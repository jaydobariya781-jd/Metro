package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * LoginActivity — Jay Dobariya
 * Authenticates the user via phone number.
 * Validates input in real-time and routes to VerifyOtpActivity on submit.
 * For demo: any 10-digit phone number is accepted.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        setupRealTimeValidation()
        setupClickListeners()
    }

    private fun initViews() {
        etPhone = findViewById(R.id.et_login_phone)
        btnLogin = findViewById(R.id.btn_login_submit)
        tvGoToRegister = findViewById(R.id.tv_go_to_register)
    }

    private fun setupRealTimeValidation() {
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = s?.toString()?.trim() ?: ""
                btnLogin.isEnabled = phone.length == 10 && phone.all { it.isDigit() }
                if (phone.isNotEmpty() && !phone.all { it.isDigit() }) {
                    etPhone.error = "Enter digits only"
                }
            }
        })
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val phone = etPhone.text.toString().trim()
            when {
                phone.isEmpty() -> {
                    etPhone.error = "Phone number is required"
                    etPhone.requestFocus()
                }
                phone.length != 10 -> {
                    etPhone.error = "Enter a valid 10-digit phone number"
                    etPhone.requestFocus()
                }
                !phone.all { it.isDigit() } -> {
                    etPhone.error = "Only digits allowed"
                    etPhone.requestFocus()
                }
                else -> {
                    TicketManager.setUserPhone(this, "+91 $phone")
                    Toast.makeText(this, "Sending OTP to +91 $phone…", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, VerifyOtpActivity::class.java)
                    intent.putExtra("PHONE", phone)
                    startActivity(intent)
                }
            }
        }

        tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Admin shortcut — long-press login button
        btnLogin.setOnLongClickListener {
            startActivity(Intent(this, admin.AdminActivity::class.java))
            true
        }
    }
}
