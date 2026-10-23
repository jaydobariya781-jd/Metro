package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

/**
 * MapFragment
 * Displays the official Ahmedabad Metro network map, search bar,
 * and nearby stations with instant booking shortcuts.
 */
class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNearbyStations(view)
    }

    private fun setupNearbyStations(view: View) {
        view.findViewById<CardView>(R.id.card_station_old_high_court)?.setOnClickListener {
            val intent = Intent(requireContext(), BookTicketActivity::class.java).apply {
                putExtra("FROM_STATION", "Old High Court")
            }
            startActivity(intent)
        }

        view.findViewById<CardView>(R.id.card_station_sabarmati)?.setOnClickListener {
            val intent = Intent(requireContext(), BookTicketActivity::class.java).apply {
                putExtra("FROM_STATION", "Sabarmati")
            }
            startActivity(intent)
        }

        view.findViewById<TextView>(R.id.tv_see_all_nearby)?.setOnClickListener {
            Toast.makeText(requireContext(), "Showing all 32 Ahmedabad Metro stations across Blue & Red lines", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<CardView>(R.id.card_search)?.setOnClickListener {
            startActivity(Intent(requireContext(), SearchTrainsActivity::class.java))
        }
    }
}
