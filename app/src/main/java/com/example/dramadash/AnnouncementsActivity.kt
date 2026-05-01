package com.example.dramadash

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnnouncementsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvAnnouncementsTitle: TextView
    private lateinit var tvAnnouncementsContent: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)

        // Initialize views
        btnBack = findViewById(R.id.btnBack)
        tvAnnouncementsTitle = findViewById(R.id.tvAnnouncementsTitle)
        tvAnnouncementsContent = findViewById(R.id.tvAnnouncementsContent)

        // ✅ BACK BUTTON (put this early)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        dbHelper = DatabaseHelper(this)

        tvAnnouncementsTitle.text = "Announcements"

        val username = intent.getStringExtra("username")

        if (username == null) {
            tvAnnouncementsContent.text = "Error loading announcements."
            return
        }

        val groupName = dbHelper.getUserGroup(username)

        if (groupName == null) {
            tvAnnouncementsContent.text = "No group found."
            return
        }

        val cursor = dbHelper.getAnnouncementsByGroup(groupName)

        if (cursor.count == 0) {
            tvAnnouncementsContent.text = "No announcements yet for $groupName."
        } else {
            val builder = StringBuilder()

            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val message = cursor.getString(cursor.getColumnIndexOrThrow("message"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date_posted"))

                builder.append("Title: $title\n")
                builder.append("Date: $date\n")
                builder.append("$message\n\n")
            }

            tvAnnouncementsContent.text = "Group: $groupName\n\n$builder"
        }

        cursor.close()
    }
}