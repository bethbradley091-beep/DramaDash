package com.example.dramadash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnAnnouncements: Button
    private lateinit var btnTimetable: Button
    private lateinit var btnPayments: Button
    private lateinit var btnEvents: Button
    private lateinit var btnLogout: Button
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnAnnouncements = findViewById(R.id.btnAnnouncements)
        btnTimetable = findViewById(R.id.btnTimetable)
        btnPayments = findViewById(R.id.btnPayments)
        btnEvents = findViewById(R.id.btnEvents)
        btnLogout = findViewById(R.id.btnLogout)

        preferences = getSharedPreferences("DramaDashPrefs", MODE_PRIVATE)

        val studentName = preferences.getString("studentName", "User")
        val day = preferences.getString("selectedDay", "Not selected")
        val time = preferences.getString("selectedTime", "Not selected")
        val location = preferences.getString("selectedLocation", "Not selected")
        val username = preferences.getString("loggedInUsername", "")

        tvWelcome.text = "Welcome, $studentName\n\nYour drama class:\n$day at $time\n$location"

        btnAnnouncements.setOnClickListener {
            val intent = Intent(this, AnnouncementsActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        btnTimetable.setOnClickListener {
            val intent = Intent(this, TimetableActivity::class.java)
            startActivity(intent)
        }

        btnPayments.setOnClickListener {
            val intent = Intent(this, PaymentsActivity::class.java)
            startActivity(intent)
        }

        btnEvents.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val editor = preferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}