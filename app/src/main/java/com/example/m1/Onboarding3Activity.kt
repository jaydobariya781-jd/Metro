package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        findViewById<Button>(R.id.btn_get_started)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
