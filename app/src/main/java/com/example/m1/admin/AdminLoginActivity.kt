package com.example.m1.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.m1.R

/**
 * AdminLoginActivity – Alok Kumar
 * Secure admin login screen with password gate.
 * Demo password: admin123
 * Access: From Profile screen > Admin Panel option.
 */
class AdminLoginActivity : AppCompatActivity() {

    private lateinit var etAdminPassword: EditText
    private lateinit var btnAdminLogin: Button
    private lateinit var tvForgotPin: TextView
    private lateinit var ivBack: ImageView

    // Demo admin credentials
    private val ADMIN_PASSWORD = "admin123"
    private val ADMIN_EMAIL = "admin@metro.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        initViews()
        setupRealTimeValidation()
        setupClickListeners()
    }

    private fun initViews() {
        etAdminPassword = findViewById(R.id.et_admin_password)
        btnAdminLogin = findViewById(R.id.btn_admin_login)
        tvForgotPin = findViewById(R.id.tv_forgot_admin_pin)
        ivBack = findViewById(R.id.iv_admin_login_back)
        btnAdminLogin.isEnabled = false
    }

    private fun setupRealTimeValidation() {
        etAdminPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s?.toString()?.trim() ?: ""
                btnAdminLogin.isEnabled = password.length >= 6
            }
        })
    }

    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            finish()
        }

        btnAdminLogin.setOnClickListener {
            val password = etAdminPassword.text.toString().trim()
            when {
                password.isEmpty() -> {
                    etAdminPassword.error = "Password is required"
                    etAdminPassword.requestFocus()
                }
                password == ADMIN_PASSWORD -> {
                    Toast.makeText(this, "Welcome, Metro Administrator!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                else -> {
                    etAdminPassword.error = "Incorrect admin password"
                    etAdminPassword.requestFocus()
                    Toast.makeText(this, "Access denied. Wrong password.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvForgotPin.setOnClickListener {
            Toast.makeText(
                this,
                "Contact Metro IT Support: admin@metro.com",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
