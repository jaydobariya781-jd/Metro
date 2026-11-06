package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

/**
 * MapActivity — Brij Nagvadiya
 * Standalone activity showing the Ahmedabad Metro route map image.
 * Accessible from the bottom navigation or profile menu.
 */
class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        findViewById<LinearLayout>(R.id.nav_home)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.nav_map)?.setOnClickListener { /* Already here */ }
        findViewById<LinearLayout>(R.id.nav_profile)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}
