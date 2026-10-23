package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.m1.admin.AdminActivity

/**
 * HomeFragment
 * Encapsulates the home dashboard screen: top greeting, admin shortcut,
 * notification icon, 2x2 quick action feature cards, and dynamic recent ticket bookings.
 */
class HomeFragment : Fragment() {

    private lateinit var llRecentTicketsContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llRecentTicketsContainer = view.findViewById(R.id.ll_recent_tickets_container)

        setupListeners(view)
        loadRecentTickets()
    }

    override fun onResume() {
        super.onResume()
        loadRecentTickets()
    }

    private fun setupListeners(view: View) {
        val openBooking = {
            startActivity(Intent(requireContext(), BookTicketActivity::class.java))
        }

        // Admin Portal Icon
        view.findViewById<ImageView>(R.id.iv_admin_panel)?.setOnClickListener {
            startActivity(Intent(requireContext(), AdminActivity::class.java))
            Toast.makeText(requireContext(), "Welcome to Metro Admin Console", Toast.LENGTH_SHORT).show()
        }

        // 2x2 Feature Grid
        view.findViewById<CardView>(R.id.card_book_ticket)?.setOnClickListener { openBooking() }

        view.findViewById<CardView>(R.id.card_my_tickets)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(1)
        }

        view.findViewById<CardView>(R.id.card_route_map)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(2)
        }

        view.findViewById<CardView>(R.id.card_profile_menu)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(3)
        }

        // Notification Icon
        val openNotifications = {
            startActivity(Intent(requireContext(), NotificationsActivity::class.java))
        }
        view.findViewById<ImageView>(R.id.iv_notification)?.setOnClickListener { openNotifications() }
        view.findViewById<View>(R.id.layout_notification_bell)?.setOnClickListener { openNotifications() }

        // User Avatar click switches to profile
        view.findViewById<ImageView>(R.id.iv_avatar)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(3)
        }

        // Recent Bookings - See All switches to Tickets tab
        view.findViewById<TextView>(R.id.tv_see_all)?.setOnClickListener {
            (activity as? MainActivity)?.selectTab(1)
        }
    }

    fun loadRecentTickets() {
        val context = context ?: return
        llRecentTicketsContainer.removeAllViews()
        val tickets = TicketManager.getAllTickets(context).take(2)
        val inflater = LayoutInflater.from(context)

        for (ticket in tickets) {
            val cardView = inflater.inflate(R.layout.item_ticket_card, llRecentTicketsContainer, false)

            cardView.findViewById<TextView>(R.id.tv_ticket_route).text =
                "${ticket.fromStation} → ${ticket.toStation}"

            val passLabel = if (ticket.passengers == 1) "1 Pass." else "${ticket.passengers} Pass."
            cardView.findViewById<TextView>(R.id.tv_ticket_datetime).text =
                "${ticket.date} • ${ticket.time} • $passLabel"

            cardView.findViewById<TextView>(R.id.tv_ticket_id).text =
                "ID: ${ticket.ticketId}"

            cardView.findViewById<TextView>(R.id.tv_ticket_fare).text =
                "₹${"%.2f".format(ticket.fare)}"

            val tvStatus = cardView.findViewById<TextView>(R.id.tv_ticket_status)
            tvStatus.text = ticket.status

            cardView.setOnClickListener {
                val intent = Intent(requireContext(), BookingSuccessActivity::class.java).apply {
                    putExtra("TICKET_ID", ticket.ticketId)
                }
                startActivity(intent)
            }

            llRecentTicketsContainer.addView(cardView)
        }
    }
}
