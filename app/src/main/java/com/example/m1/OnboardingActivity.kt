package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * OnboardingActivity — Jay Dobariya
 * Two-slide onboarding carousel highlighting Metro benefits.
 * Users can advance slides or skip directly to Login.
 */
class OnboardingActivity : AppCompatActivity() {

    private var currentSlide = 0
    private val slides = listOf(
        Triple("Fast & Reliable", "Ahmedabad Metro connects 38+ stations across the city. Enjoy AC coaches, real-time tracking, and zero traffic delays.", R.drawable.metro),
        Triple("Easy Booking", "Book tickets in seconds. Select station, choose passengers, pay via UPI, card or cash — and you're ready to go!", R.drawable.train_material__1_)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        renderSlide()
        setupClickListeners()
    }

    private fun renderSlide() {
        val (title, desc, imgRes) = slides[currentSlide]
        findViewById<TextView>(R.id.tv_onboarding_title)?.text = title
        findViewById<TextView>(R.id.tv_onboarding_desc)?.text = desc
        findViewById<ImageView>(R.id.iv_onboarding_image)?.setImageResource(imgRes)

        // Update dot indicators
        val dot1 = findViewById<ImageView>(R.id.dot_1)
        val dot2 = findViewById<ImageView>(R.id.dot_2)
        dot1?.setImageResource(if (currentSlide == 0) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive)
        dot2?.setImageResource(if (currentSlide == 1) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive)

        // Button label
        val isLast = currentSlide == slides.lastIndex
        findViewById<Button>(R.id.btn_onboarding_next)?.text = if (isLast) "Get Started" else "Next →"
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.btn_onboarding_next)?.setOnClickListener {
            if (currentSlide < slides.lastIndex) {
                currentSlide++
                renderSlide()
            } else {
                startActivity(Intent(this, Onboarding3Activity::class.java))
            }
        }
        findViewById<TextView>(R.id.tv_onboarding_skip)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
