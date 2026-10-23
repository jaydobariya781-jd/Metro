package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * MainActivity — Jay Dobariya
 * Bottom navigation host for the Metro passenger app.
 * Hosts 4 fragments: Home, My Tickets, Map, Profile.
 * Manages fragment back-stack and tab state.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navTickets: LinearLayout
    private lateinit var navMap: LinearLayout
    private lateinit var navProfile: LinearLayout

    val homeFragment: HomeFragment by lazy { HomeFragment() }
    val ticketsFragment: TicketsFragment by lazy { TicketsFragment() }
    val mapFragment: MapFragment by lazy { MapFragment() }
    val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    private var activeTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initBottomNav()
        if (savedInstanceState == null) {
            loadFragment(homeFragment, 0)
        }
        setupBottomNavListeners()
        setupBackNavigation()
    }

    private fun initBottomNav() {
        navHome = findViewById(R.id.nav_home)
        navTickets = findViewById(R.id.nav_tickets)
        navMap = findViewById(R.id.nav_map)
        navProfile = findViewById(R.id.nav_profile)
    }

    private fun setupBottomNavListeners() {
        navHome.setOnClickListener { selectTab(0) }
        navTickets.setOnClickListener { selectTab(1) }
        navMap.setOnClickListener { selectTab(2) }
        navProfile.setOnClickListener { selectTab(3) }
    }

    /**
     * Switches to a tab. Called internally and from child fragments.
     * @param tab 0=Home, 1=Tickets, 2=Map, 3=Profile
     * @param isHistory when selecting Tickets tab, whether to open history sub-tab
     */
    fun selectTab(tab: Int, isHistory: Boolean = false) {
        if (tab == activeTab && tab != 1) return
        activeTab = tab
        val fragment: Fragment = when (tab) {
            0 -> homeFragment
            1 -> {
                ticketsFragment.setInitialTab(isHistory)
                ticketsFragment
            }
            2 -> mapFragment
            3 -> profileFragment
            else -> homeFragment
        }
        loadFragment(fragment, tab)
        updateNavHighlight(tab)
    }

    private fun loadFragment(fragment: Fragment, tab: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
        updateNavHighlight(tab)
    }

    private fun updateNavHighlight(selectedTab: Int) {
        val navItems = listOf(navHome, navTickets, navMap, navProfile)
        for (i in navItems.indices) {
            navItems[i].alpha = if (i == selectedTab) 1.0f else 0.5f
        }
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (activeTab != 0) {
                        selectTab(0)
                    } else {
                        finish()
                    }
                }
            }
        )
    }
}
