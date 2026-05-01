package com.example.dramadash

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectStudentActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvWelcomeStudent: TextView
    private lateinit var tvClassInfo: TextView
    private lateinit var spinnerDay: Spinner
    private lateinit var spinnerTime: Spinner
    private lateinit var spinnerLocation: Spinner
    private lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_student)

        btnBack = findViewById(R.id.btnBack)
        tvWelcomeStudent = findViewById(R.id.tvWelcomeStudent)
        tvClassInfo = findViewById(R.id.tvClassInfo)
        spinnerDay = findViewById(R.id.spinnerDay)
        spinnerTime = findViewById(R.id.spinnerTime)
        spinnerLocation = findViewById(R.id.spinnerLocation)
        btnContinue = findViewById(R.id.btnContinue)

        val studentName = intent.getStringExtra("studentName")

        if (!studentName.isNullOrEmpty()) {
            tvWelcomeStudent.text = "Welcome, $studentName"
        } else {
            tvWelcomeStudent.text = "Welcome"
        }

        tvClassInfo.text = "Please choose your drama class day, time and location."

        val days = arrayOf("Choose Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val times = arrayOf("Choose Time", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM")
        val locations = arrayOf(
            "Choose Location",
            "The Playhouse",
            "The Vale Centre",
            "The Gasyard",
            "The Waterside Shared Village",
            "Cumber"
        )

        spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)
        spinnerTime.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, times)
        spinnerLocation.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)

        btnBack.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val selectedDay = spinnerDay.selectedItem.toString()
            val selectedTime = spinnerTime.selectedItem.toString()
            val selectedLocation = spinnerLocation.selectedItem.toString()

            if (selectedDay == "Choose Day" || selectedTime == "Choose Time" || selectedLocation == "Choose Location") {
                Toast.makeText(this, "Please select all options", Toast.LENGTH_SHORT).show()
            } else {

                val prefs = getSharedPreferences("DramaDashPrefs", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("studentName", studentName)
                editor.putString("selectedDay", selectedDay)
                editor.putString("selectedTime", selectedTime)
                editor.putString("selectedLocation", selectedLocation)
                editor.apply()

                Toast.makeText(this, "Class selection saved", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("studentName", studentName)
                startActivity(intent)
                finish()
            }
        }
    }
}