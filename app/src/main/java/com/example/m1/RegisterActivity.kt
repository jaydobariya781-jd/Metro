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
 * Collects name and phone; then routes to OTP verification.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
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
        etName = findViewById(R.id.et_full_name)
        etPhone = findViewById(R.id.et_mobile)
        btnRegister = findViewById(R.id.btn_register)
        tvGoToLogin = findViewById(R.id.tv_login_link)
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
    }

    private fun validateFields(): Boolean {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        val nameOk = name.length >= 2
        val phoneOk = phone.length == 10 && phone.all { it.isDigit() }

        btnRegister.isEnabled = nameOk && phoneOk
        return nameOk && phoneOk
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            // Final field-level validation with focused error messages
            if (!UserValidationUtils.isValidName(name)) {
                etName.error = "Enter your full name (at least 2 characters)"
                etName.requestFocus()
                return@setOnClickListener
            }
            if (!UserValidationUtils.isValidPhone(phone)) {
                etPhone.error = "Enter a valid 10-digit mobile number"
                etPhone.requestFocus()
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
