package com.example.dramadash

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvEventsTitle: TextView
    private lateinit var tvEventsContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        btnBack = findViewById(R.id.btnBack)
        tvEventsTitle = findViewById(R.id.tvEventsTitle)
        tvEventsContent = findViewById(R.id.tvEventsContent)

        val prefs = getSharedPreferences("DramaDashPrefs", MODE_PRIVATE)
        val day = prefs.getString("selectedDay", "")
        val time = prefs.getString("selectedTime", "")
        val location = prefs.getString("selectedLocation", "")

        tvEventsTitle.text = "Events"

        val eventDetails = when {
            location == "The Playhouse" ->
                "Upcoming Event:\nSpring Showcase\nDate: 20 May\nTime: 6:30 PM\n\nPlease arrive 30 minutes early."

            location == "The Vale Centre" ->
                "Upcoming Event:\nDrama Skills Workshop\nDate: 25 May\nTime: 5:00 PM\n\nBring notebook and water bottle."

            location == "The Gasyard" ->
                "Upcoming Event:\nMini Performance Night\nDate: 30 May\nTime: 7:00 PM\n\nFamily and friends welcome."

            location == "The Waterside Shared Village" ->
                "Upcoming Event:\nParent Viewing Evening\nDate: 2 June\nTime: 7:30 PM\n\nStudents should wear black rehearsal clothes."

            location == "Cumber" ->
                "Upcoming Event:\nFeis Preparation Session\nDate: 8 June\nTime: 4:30 PM\n\nPlease bring scripts and character notes."

            else ->
                "No events available yet for this class."
        }

        tvEventsContent.text = "Class: $day at $time\nLocation: $location\n\n$eventDetails"

        btnBack.setOnClickListener {
            finish()
        }
    }
}