package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Onboarding3Activity — Jay Dobariya
 * Final onboarding screen: "Your Metro, Your Way".
 * CTA buttons route to Login or Register.
 */
class Onboarding3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding3)

        // "Login" button
        findViewById<Button>(R.id.btn_onboarding_login)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // "Create Account" button
        findViewById<Button>(R.id.btn_onboarding_register)?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        // "Skip" / already have account text
        findViewById<TextView>(R.id.tv_onboarding3_skip)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
