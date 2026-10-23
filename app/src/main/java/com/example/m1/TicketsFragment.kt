package com.example.m1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * TicketsFragment
 * Manages user ticket lists with Active and History switcher tabs,
 * ticket detail navigation, and quick booking access.
 */
class TicketsFragment : Fragment() {

    private lateinit var llTicketsContainer: LinearLayout
    private lateinit var tabActive: TextView
    private lateinit var tabHistory: TextView

    private var isHistoryTab = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llTicketsContainer = view.findViewById(R.id.ll_tickets_container)
        tabActive = view.findViewById(R.id.tab_active)
        tabHistory = view.findViewById(R.id.tab_history)

        setupTabs()

        view.findViewById<Button>(R.id.btn_book_new_ticket)?.setOnClickListener {
            startActivity(Intent(requireContext(), BookTicketActivity::class.java))
        }

        if (isHistoryTab) {
            selectHistoryTab()
        } else {
            selectActiveTab()
        }

        loadTickets()
    }

    override fun onResume() {
        super.onResume()
        loadTickets()
    }

    fun setInitialTab(isHistory: Boolean) {
        this.isHistoryTab = isHistory
        if (::tabActive.isInitialized && ::tabHistory.isInitialized) {
            if (isHistory) selectHistoryTab() else selectActiveTab()
            loadTickets()
        }
    }

    private fun setupTabs() {
        tabActive.setOnClickListener {
            isHistoryTab = false
            selectActiveTab()
            loadTickets()
        }

        tabHistory.setOnClickListener {
            isHistoryTab = true
            selectHistoryTab()
            loadTickets()
        }
    }

    private fun selectActiveTab() {
        tabActive.setBackgroundResource(R.drawable.bg_tab_selected)
        tabActive.setTextColor(Color.parseColor("#1A56C4"))
        tabHistory.setBackgroundColor(Color.TRANSPARENT)
        tabHistory.setTextColor(Color.parseColor("#888888"))
    }

    private fun selectHistoryTab() {
        tabHistory.setBackgroundResource(R.drawable.bg_tab_selected)
        tabHistory.setTextColor(Color.parseColor("#1A56C4"))
        tabActive.setBackgroundColor(Color.TRANSPARENT)
        tabActive.setTextColor(Color.parseColor("#888888"))
    }

    fun loadTickets() {
        val context = context ?: return
        llTicketsContainer.removeAllViews()
        val tickets = if (isHistoryTab) {
            TicketManager.getHistoryTickets(context)
        } else {
            TicketManager.getActiveTickets(context)
        }

        val inflater = LayoutInflater.from(context)

        if (tickets.isEmpty()) {
            val emptyTv = TextView(context).apply {
                text = if (isHistoryTab) {
                    "No past trip history found.\nYour completed metro trips will appear here."
                } else {
                    "No active tickets right now.\nClick '+ Book' to book a new metro ticket!"
                }
                textSize = 14f
                setTextColor(Color.parseColor("#888888"))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setPadding(20, 60, 20, 40)
                setLineSpacing(4f, 1f)
            }
            llTicketsContainer.addView(emptyTv)
        } else {
            for (ticket in tickets) {
                val cardView = inflater.inflate(R.layout.item_ticket_card, llTicketsContainer, false)

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
                    val intent = Intent(context, BookingSuccessActivity::class.java).apply {
                        putExtra("TICKET_ID", ticket.ticketId)
                    }
                    startActivity(intent)
                }

                llTicketsContainer.addView(cardView)
            }
        }
    }
}
