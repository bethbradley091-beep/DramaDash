package com.example.dramadash

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminActivity : AppCompatActivity() {

    private lateinit var spinnerGroup: Spinner
    private lateinit var editTitle: EditText
    private lateinit var editMessage: EditText
    private lateinit var btnPost: Button
    private lateinit var btnLogout: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        dbHelper = DatabaseHelper(this)

        spinnerGroup = findViewById(R.id.spinnerGroup)
        editTitle = findViewById(R.id.editTitle)
        editMessage = findViewById(R.id.editMessage)
        btnPost = findViewById(R.id.btnPost)
        btnLogout = findViewById(R.id.btnLogout)

        val groups = arrayOf(
            "Vale Centre - Junior Group",
            "Vale Centre - Senior Group",
            "The Playhouse - Junior Group",
            "The Playhouse - Senior Group",
            "Cumber - Junior Group",
            "Cumber - Senior Group",
            "The Waterside Shared Village - Junior Group",
            "The Waterside Shared Village - Senior Group",
            "The Gasyard - Junior Group",
            "The Gasyard - Senior Group"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            groups
        )

        spinnerGroup.adapter = adapter

        // POST BUTTON
        btnPost.setOnClickListener {
            val group = spinnerGroup.selectedItem.toString()
            val title = editTitle.text.toString().trim()
            val message = editMessage.text.toString().trim()

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            val inserted = dbHelper.insertAnnouncement(group, title, message, date)

            if (inserted) {
                Toast.makeText(this, "Announcement added", Toast.LENGTH_SHORT).show()
                editTitle.setText("")
                editMessage.setText("")
            } else {
                Toast.makeText(this, "Failed to add announcement", Toast.LENGTH_SHORT).show()
            }
        }

        // LOGOUT BUTTON → GO TO HOME (MainActivity)
        btnLogout.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // closes admin screen
        }
    }
}