package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.m1.admin.AdminActivity

/**
 * ProfileFragment
 * Displays user commuter details, trip history shortcuts, saved stations,
 * Daily Pass activation, settings, help & support, and admin console access.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPhone = TicketManager.getUserPhone(requireContext())
        view.findViewById<TextView>(R.id.tv_profile_phone)?.text = userPhone

        setupMenuActions(view)
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<TextView>(R.id.tv_profile_phone)?.text = TicketManager.getUserPhone(requireContext())
    }

    private fun setupMenuActions(view: View) {
        view.findViewById<ImageView>(R.id.btn_edit_profile)?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.row_my_tickets)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(1, isHistory = false)
        }

        view.findViewById<LinearLayout>(R.id.row_trip_history)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(1, isHistory = true)
        }

        view.findViewById<LinearLayout>(R.id.row_saved_stations)?.setOnClickListener {
            startActivity(Intent(requireContext(), SavedStationsActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.row_payment_methods)?.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Daily Pass Purchase...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), DailyPassOtpActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.row_settings)?.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.row_help)?.setOnClickListener {
            startActivity(Intent(requireContext(), HelpSupportActivity::class.java))
        }

        view.findViewById<LinearLayout>(R.id.row_admin_panel)?.setOnClickListener {
            startActivity(Intent(requireContext(), AdminActivity::class.java))
        }

        view.findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            TicketManager.logout(requireContext())
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            activity?.finish()
        }
    }
}
