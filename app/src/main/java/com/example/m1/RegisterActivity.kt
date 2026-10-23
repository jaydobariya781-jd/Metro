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
 * RegisterActivity — Jay Dobariya
 * New user registration with real-time field validation.
 * Collects name, phone, and email; then routes to OTP verification.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        setupRealtimeValidation()
        setupClickListeners()
    }

    private fun initViews() {
        etName = findViewById(R.id.et_register_name)
        etPhone = findViewById(R.id.et_register_phone)
        etEmail = findViewById(R.id.et_register_email)
        btnRegister = findViewById(R.id.btn_register_submit)
        tvGoToLogin = findViewById(R.id.tv_go_to_login)
    }

    private fun setupRealtimeValidation() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
        }
        etName.addTextChangedListener(watcher)
        etPhone.addTextChangedListener(watcher)
        etEmail.addTextChangedListener(watcher)
    }

    private fun validateFields(): Boolean {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()

        val nameOk = name.length >= 2
        val phoneOk = phone.length == 10 && phone.all { it.isDigit() }
        val emailOk = email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        btnRegister.isEnabled = nameOk && phoneOk && emailOk
        return nameOk && phoneOk && emailOk
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            // Final field-level validation with focused error messages
            if (name.length < 2) {
                etName.error = "Enter your full name (at least 2 characters)"
                etName.requestFocus()
                return@setOnClickListener
            }
            if (phone.length != 10 || !phone.all { it.isDigit() }) {
                etPhone.error = "Enter a valid 10-digit mobile number"
                etPhone.requestFocus()
                return@setOnClickListener
            }
            if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email address"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            TicketManager.setUserName(this, name)
            TicketManager.setUserPhone(this, "+91 $phone")
            Toast.makeText(this, "OTP sent to +91 $phone", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, VerifyOtpActivity::class.java)
            intent.putExtra("PHONE", phone)
            intent.putExtra("IS_NEW_USER", true)
            startActivity(intent)
        }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
