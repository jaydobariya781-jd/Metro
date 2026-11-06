package com.example.m1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * BookTicketActivity — Brij Nagvadiya
 * Allows the user to:
 *  1. Select FROM and TO stations via spinners (synced with Admin additions & renames)
 *  2. Select Journey Type (Single / Return)
 *  3. Adjust passenger count (1–6)
 *  4. View dynamically calculated fare instantly
 *  5. Proceed to PaymentActivity
 */
class BookTicketActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvPassengerCount: TextView
    private lateinit var tvFareAmount: TextView
    private lateinit var tvBarTotalFare: TextView
    private lateinit var btnMinus: ImageView
    private lateinit var btnPlus: ImageView
    private lateinit var btnProceedToPayment: Button
    private lateinit var tvDistance: TextView
    private lateinit var rgJourneyType: RadioGroup
    private lateinit var rbReturn: RadioButton
    private lateinit var rbSingle: RadioButton

    private var passengerCount = 1
    private var fromStation = ""
    private var toStation = ""
    private var stationList: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ticket)

        initViews()
        setupHeader()
        loadStationsAndSetupSpinners()
        setupJourneyType()
        setupPassengerControls()
        setupProceedButton()
    }

    override fun onResume() {
        super.onResume()
        // Refresh stations in case Admin added or changed station names
        refreshStationsIfChanged()
    }

    private fun initViews() {
        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        tvPassengerCount = findViewById(R.id.tv_passenger_count)
        tvFareAmount = findViewById(R.id.tv_total_fare)
        tvBarTotalFare = findViewById(R.id.tv_bar_total_fare)
        btnMinus = findViewById(R.id.btn_minus_passenger)
        btnPlus = findViewById(R.id.btn_plus_passenger)
        btnProceedToPayment = findViewById(R.id.btn_proceed_payment)
        tvDistance = findViewById(R.id.tv_fare_breakdown_label)
        rgJourneyType = findViewById(R.id.rg_journey_type)
        rbSingle = findViewById(R.id.rb_single)
        rbReturn = findViewById(R.id.rb_return)
    }

    private fun setupHeader() {
        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener { finish() }
    }

    private fun loadStationsAndSetupSpinners() {
        stationList = MetroData.getAllStations(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stationList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Check if intent provided initial stations
        val intentFrom = intent.getStringExtra("FROM_STATION")
        val intentTo = intent.getStringExtra("TO_STATION")

        val fromIndex = if (!intentFrom.isNullOrEmpty()) {
            stationList.indexOfFirst { it.equals(intentFrom, ignoreCase = true) }.coerceAtLeast(0)
        } else {
            // Default to "Thaltej" if present, else 0
            val thaltejIdx = stationList.indexOfFirst { it.equals("Thaltej", ignoreCase = true) }
            if (thaltejIdx != -1) thaltejIdx else 0
        }

        val toIndex = if (!intentTo.isNullOrEmpty()) {
            stationList.indexOfFirst { it.equals(intentTo, ignoreCase = true) }.coerceAtLeast(0)
        } else {
            // Default to "Vastral Gam" if present, else last or index 5
            val vastralIdx = stationList.indexOfFirst { it.equals("Vastral Gam", ignoreCase = true) }
            if (vastralIdx != -1) vastralIdx else (if (stationList.size > 5) 5 else stationList.lastIndex)
        }

        spinnerFrom.setSelection(fromIndex)
        spinnerTo.setSelection(toIndex)

        fromStation = if (fromIndex in stationList.indices) stationList[fromIndex] else ""
        toStation = if (toIndex in stationList.indices) stationList[toIndex] else ""

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinnerFrom.selectedItem != null) {
                    fromStation = spinnerFrom.selectedItem.toString()
                }
                if (spinnerTo.selectedItem != null) {
                    toStation = spinnerTo.selectedItem.toString()
                }
                updateFare()
            }
        }
        spinnerFrom.onItemSelectedListener = spinnerListener
        spinnerTo.onItemSelectedListener = spinnerListener

        // Swap stations button
        findViewById<ImageView>(R.id.btn_swap_stations)?.setOnClickListener {
            val fromPos = spinnerFrom.selectedItemPosition
            val toPos = spinnerTo.selectedItemPosition
            spinnerFrom.setSelection(toPos)
            spinnerTo.setSelection(fromPos)
        }

        updateFare()
    }

    private fun refreshStationsIfChanged() {
        val latestStations = MetroData.getAllStations(this)
        if (latestStations != stationList) {
            val savedFrom = fromStation
            val savedTo = toStation

            stationList = latestStations
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stationList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter

            val newFromIdx = stationList.indexOfFirst { it.equals(savedFrom, ignoreCase = true) }.coerceAtLeast(0)
            val newToIdx = stationList.indexOfFirst { it.equals(savedTo, ignoreCase = true) }.coerceAtLeast(0)

            spinnerFrom.setSelection(newFromIdx)
            spinnerTo.setSelection(newToIdx)

            fromStation = if (newFromIdx in stationList.indices) stationList[newFromIdx] else ""
            toStation = if (newToIdx in stationList.indices) stationList[newToIdx] else ""
            updateFare()
        }
    }

    private fun setupJourneyType() {
        rgJourneyType.setOnCheckedChangeListener { _, _ ->
            updateFare()
        }
    }

    private fun setupPassengerControls() {
        tvPassengerCount.text = passengerCount.toString()
        btnMinus.setOnClickListener {
            if (passengerCount > 1) {
                passengerCount--
                tvPassengerCount.text = passengerCount.toString()
                updateFare()
            } else {
                Toast.makeText(this, "Minimum 1 passenger", Toast.LENGTH_SHORT).show()
            }
        }
        btnPlus.setOnClickListener {
            if (passengerCount < 6) {
                passengerCount++
                tvPassengerCount.text = passengerCount.toString()
                updateFare()
            } else {
                Toast.makeText(this, "Maximum 6 passengers per booking", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Automatically calculates and displays station-to-station ticket price
     * using MetroData and FareCalculator rules.
     */
    private fun updateFare() {
        val isReturn = rbReturn.isChecked

        if (fromStation.equals(toStation, ignoreCase = true)) {
            val minFare = 5.0 * passengerCount
            val finalFare = if (isReturn) minFare * 2 * (1.0 - FareCalculator.RETURN_DISCOUNT_PERCENT) else minFare
            val formatted = formatPrice(finalFare)
            tvFareAmount.text = formatted
            tvBarTotalFare.text = formatted
            tvDistance.text = "Same station (Platform pass)"
            return
        }

        val fare = MetroData.calculateFare(fromStation, toStation, passengerCount, isReturn = isReturn)
        val formatted = formatPrice(fare)

        tvFareAmount.text = formatted
        tvBarTotalFare.text = formatted

        val journeyTypeStr = if (isReturn) "Return Journey" else "Single Journey"
        val passStr = "$passengerCount Passenger${if (passengerCount > 1) "s" else ""}"
        tvDistance.text = "$journeyTypeStr • $passStr"
    }

    private fun formatPrice(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            "₹${amount.toInt()}"
        } else {
            String.format("₹%.2f", amount)
        }
    }

    private fun setupProceedButton() {
        btnProceedToPayment.setOnClickListener {
            if (fromStation.equals(toStation, ignoreCase = true)) {
                Toast.makeText(this, "Origin and destination cannot be the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val isReturn = rbReturn.isChecked
            val fare = MetroData.calculateFare(fromStation, toStation, passengerCount, isReturn = isReturn)
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("FROM_STATION", fromStation)
                putExtra("TO_STATION", toStation)
                putExtra("PASSENGERS", passengerCount)
                putExtra("FARE", fare)
                putExtra("IS_RETURN", isReturn)
            }
            startActivity(intent)
        }
    }
}
