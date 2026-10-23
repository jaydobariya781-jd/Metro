package com.example.m1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * SplashActivity — Jay Dobariya
 * App entry point: displays Metro logo with a fade-in animation,
 * then routes to OnboardingActivity (first launch) or MainActivity (returning user).
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animate logo and tagline with a smooth fade-in
        val ivLogo = findViewById<ImageView>(R.id.iv_splash_logo)
        val tvTagline = findViewById<TextView>(R.id.tv_splash_tagline)
        val tvVersion = findViewById<TextView>(R.id.tv_splash_version)

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in).apply {
            duration = 900
        }
        ivLogo?.startAnimation(fadeIn)
        tvTagline?.startAnimation(fadeIn)
        tvVersion?.startAnimation(fadeIn)

        // Route after 2200ms — to Onboarding if first-time, else directly to home
        Handler(Looper.getMainLooper()).postDelayed({
            val destination = if (TicketManager.isLoggedIn(this)) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
            destination.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(destination)
            finish()
        }, 2200L)
    }
}
