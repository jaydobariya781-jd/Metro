package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

/**
 * Saved Stations Activity
 * Matches the passenger app "Saved Stations Redesign" screen with interactive
 * Favorites (Home, Work), real-time distance and train arrival indicators,
 * live search filtering, recent search history management, and Add Station FAB.
 */
class SavedStationsActivity : AppCompatActivity() {

    private lateinit var etSearchSaved: EditText
    private lateinit var cardFavHome: CardView
    private lateinit var cardFavWork: CardView
    private lateinit var cardRecent1: CardView
    private lateinit var cardRecent2: CardView
    private lateinit var cardRecent3: CardView
    private lateinit var llRecentContainer: LinearLayout

    private lateinit var tvHomeLabel: TextView
    private lateinit var tvHomeStation: TextView
    private lateinit var tvWorkLabel: TextView
    private lateinit var tvWorkStation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_stations)

        initViews()
        setupHeader()
        setupSearch()
        setupFavorites()
        setupRecentSearches()
        setupFab()
        setupBottomNav()
    }

    private fun initViews() {
        etSearchSaved = findViewById(R.id.et_search_saved)
        cardFavHome = findViewById(R.id.card_fav_home)
        cardFavWork = findViewById(R.id.card_fav_work)
        cardRecent1 = findViewById(R.id.card_recent_1)
        cardRecent2 = findViewById(R.id.card_recent_2)
        cardRecent3 = findViewById(R.id.card_recent_3)
        llRecentContainer = findViewById(R.id.ll_recent_searches_container)

        tvHomeLabel = findViewById(R.id.tv_home_label)
        tvHomeStation = findViewById(R.id.tv_home_station)
        tvWorkLabel = findViewById(R.id.tv_work_label)
        tvWorkStation = findViewById(R.id.tv_work_station)
    }

    private fun setupHeader() {
        // Back Button
        findViewById<ImageView>(R.id.btn_saved_back)?.setOnClickListener {
            finish()
        }

        // Title click
        findViewById<TextView>(R.id.tv_saved_title)?.setOnClickListener {
            Toast.makeText(this, "Ahmedabad Metro • Saved Stations", Toast.LENGTH_SHORT).show()
        }

        // Menu Options Button (⋮)
        findViewById<ImageView>(R.id.btn_saved_menu)?.setOnClickListener {
            val options = arrayOf(
                "Sort by Nearest Distance",
                "Refresh Live Arrivals",
                "Add New Custom Station",
                "Metro Line Map"
            )
            AlertDialog.Builder(this)
                .setTitle("Station Options")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> Toast.makeText(this, "Stations sorted by nearest GPS distance", Toast.LENGTH_SHORT).show()
                        1 -> {
                            findViewById<TextView>(R.id.tv_home_next_train)?.text = "Next: 2 mins"
                            findViewById<TextView>(R.id.tv_work_next_train)?.text = "Next: 9 mins"
                            Toast.makeText(this, "Live arrival times refreshed for all 32 stations", Toast.LENGTH_SHORT).show()
                        }
                        2 -> showAddStationDialog()
                        3 -> startActivity(Intent(this, MapActivity::class.java))
                    }
                }
                .show()
        }
    }

    private fun setupSearch() {
        etSearchSaved.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                filterStations(s?.toString()?.trim() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterStations(query: String) {
        val q = query.lowercase()

        // Filter Home Card
        val matchHome = "home".contains(q) || tvHomeStation.text.toString().lowercase().contains(q)
        cardFavHome.visibility = if (matchHome || q.isEmpty()) View.VISIBLE else View.GONE

        // Filter Work Card
        val matchWork = "work".contains(q) || tvWorkStation.text.toString().lowercase().contains(q)
        cardFavWork.visibility = if (matchWork || q.isEmpty()) View.VISIBLE else View.GONE

        // Filter Recent Searches
        val match1 = "central station".contains(q) || "hub for all metro lines".contains(q)
        cardRecent1.visibility = if (match1 || q.isEmpty()) View.VISIBLE else View.GONE

        val match2 = "airport terminal 4".contains(q) || "international departures".contains(q)
        cardRecent2.visibility = if (match2 || q.isEmpty()) View.VISIBLE else View.GONE

        val match3 = "university park".contains(q) || "east campus entrance".contains(q)
        cardRecent3.visibility = if (match3 || q.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupFavorites() {
        // Edit Favorites Button
        findViewById<TextView>(R.id.tv_favorites_edit)?.setOnClickListener {
            showEditFavoritesDialog()
        }

        // Home Card Click
        cardFavHome.setOnClickListener {
            showStationActionDialog(
                title = "Home: ${tvHomeStation.text}",
                distance = "1.2 km away",
                nextTrain = "3 mins (Towards Vastral Gam)",
                line = "Blue Line (East-West Corridor)"
            )
        }

        // Work Card Click
        cardFavWork.setOnClickListener {
            showStationActionDialog(
                title = "Work: ${tvWorkStation.text}",
                distance = "4.8 km away",
                nextTrain = "12 mins (Towards Thaltej)",
                line = "Red Line (North-South Corridor)"
            )
        }
    }

    private fun showStationActionDialog(title: String, distance: String, nextTrain: String, line: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(
                "• Distance: $distance\n" +
                "• Next Arrival: $nextTrain\n" +
                "• Corridor: $line\n\n" +
                "Choose an action below to proceed:"
            )
            .setPositiveButton("Book Ticket") { _, _ ->
                startActivity(Intent(this, BookTicketActivity::class.java))
            }
            .setNeutralButton("View on Map") { _, _ ->
                startActivity(Intent(this, MapActivity::class.java))
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showEditFavoritesDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val etHome = EditText(this).apply {
            hint = "Home Station Name"
            setText(tvHomeStation.text)
        }

        val etWork = EditText(this).apply {
            hint = "Work Station Name"
            setText(tvWorkStation.text)
        }

        layout.addView(TextView(this).apply { text = "Home Station:"; setPadding(0, 10, 0, 5) })
        layout.addView(etHome)
        layout.addView(TextView(this).apply { text = "Work Station:"; setPadding(0, 20, 0, 5) })
        layout.addView(etWork)

        AlertDialog.Builder(this)
            .setTitle("Edit Favorite Stations")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                if (etHome.text.isNotEmpty()) tvHomeStation.text = etHome.text.toString().trim()
                if (etWork.text.isNotEmpty()) tvWorkStation.text = etWork.text.toString().trim()
                Toast.makeText(this, "Favorite stations updated successfully!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupRecentSearches() {
        // Clear All
        findViewById<TextView>(R.id.tv_recent_clear_all)?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear Recent Searches")
                .setMessage("Are you sure you want to clear all your recent station searches?")
                .setPositiveButton("Clear") { _, _ ->
                    llRecentContainer.visibility = View.GONE
                    Toast.makeText(this, "Recent searches cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Recent 1: Central Station
        cardRecent1.setOnClickListener {
            showStationActionDialog(
                title = "Central Station (Kalupur Hub)",
                distance = "2.1 km away",
                nextTrain = "5 mins",
                line = "Ahmedabad Railway Interchange"
            )
        }

        // Recent 2: Airport Terminal 4
        cardRecent2.setOnClickListener {
            showStationActionDialog(
                title = "Airport Terminal 4",
                distance = "8.4 km away",
                nextTrain = "14 mins",
                line = "Airport Express Corridor"
            )
        }

        // Recent 3: University Park
        cardRecent3.setOnClickListener {
            showStationActionDialog(
                title = "University Park Station",
                distance = "3.6 km away",
                nextTrain = "7 mins",
                line = "West Corridor Campus Link"
            )
        }
    }

    private fun setupFab() {
        findViewById<LinearLayout>(R.id.fab_add_station)?.setOnClickListener {
            showAddStationDialog()
        }
    }

    private fun showAddStationDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val metroStations = arrayOf(
            "Thaltej",
            "Vastral Gam",
            "Old High Court",
            "Sabarmati",
            "Kalupur",
            "Motera Stadium",
            "Gandhigram",
            "Paldi",
            "Ranip",
            "AEC",
            "Kankaria East"
        )

        val spinnerStations = Spinner(this).apply {
            adapter = ArrayAdapter(this@SavedStationsActivity, android.R.layout.simple_spinner_dropdown_item, metroStations)
        }

        val etCustomLabel = EditText(this).apply {
            hint = "Custom Label (e.g. Gym, College, Parents)"
        }

        layout.addView(TextView(this).apply { text = "Select Metro Station:"; setPadding(0, 10, 0, 5) })
        layout.addView(spinnerStations)
        layout.addView(TextView(this).apply { text = "Station Tag:"; setPadding(0, 20, 0, 5) })
        layout.addView(etCustomLabel)

        AlertDialog.Builder(this)
            .setTitle("Add Station to Saved")
            .setView(layout)
            .setPositiveButton("Add Station") { _, _ ->
                val selectedStation = spinnerStations.selectedItem.toString()
                val tag = if (etCustomLabel.text.isNotEmpty()) etCustomLabel.text.toString().trim() else "Saved Station"
                Toast.makeText(this, "'$selectedStation' added to saved stations as '$tag'!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBottomNav() {
        // Tab 1: Home
        findViewById<LinearLayout>(R.id.nav_user_home)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        // Tab 2: Tickets
        findViewById<LinearLayout>(R.id.nav_user_tickets)?.setOnClickListener {
            startActivity(Intent(this, MyTicketsActivity::class.java))
            finish()
        }

        // Tab 3: Map
        findViewById<LinearLayout>(R.id.nav_user_map)?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }

        // Tab 4: Profile
        findViewById<LinearLayout>(R.id.nav_user_profile)?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
            finish()
        }
    }
}
