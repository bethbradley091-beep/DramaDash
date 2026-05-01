package com.example.dramadash

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimetableActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvTimetableTitle: TextView
    private lateinit var tvTimetableContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        btnBack = findViewById(R.id.btnBack)
        tvTimetableTitle = findViewById(R.id.tvTimetableTitle)
        tvTimetableContent = findViewById(R.id.tvTimetableContent)

        val prefs = getSharedPreferences("DramaDashPrefs", MODE_PRIVATE)
        val day = prefs.getString("selectedDay", "")
        val time = prefs.getString("selectedTime", "")
        val location = prefs.getString("selectedLocation", "")

        tvTimetableTitle.text = "Timetable"

        val timetableDetails = when {
            day == "Monday" && time == "4:00 PM" && location == "The Playhouse" ->
                "Class: Junior Speech and Drama\nTime: Monday at 4:00 PM\nLocation: The Playhouse\nTeacher: Beth Bradley"

            day == "Tuesday" && time == "5:00 PM" && location == "The Vale Centre" ->
                "Class: Intermediate Drama Group\nTime: Tuesday at 5:00 PM\nLocation: The Vale Centre\nTeacher: Beth Bradley"

            day == "Wednesday" && time == "6:00 PM" && location == "The Gasyard" ->
                "Class: Senior Performance Group\nTime: Wednesday at 6:00 PM\nLocation: The Gasyard\nTeacher: Beth Bradley"

            day == "Thursday" && time == "7:00 PM" && location == "The Waterside Shared Village" ->
                "Class: Advanced Drama Class\nTime: Thursday at 7:00 PM\nLocation: The Waterside Shared Village\nTeacher: Beth Bradley"

            location == "Cumber" ->
                "Class: Cumber Drama Group\nTime: $day at $time\nLocation: Cumber\nTeacher: Beth Bradley"

            else ->
                "No timetable details available yet for this class."
        }

        tvTimetableContent.text = timetableDetails

        btnBack.setOnClickListener {
            finish()
        }
    }
}