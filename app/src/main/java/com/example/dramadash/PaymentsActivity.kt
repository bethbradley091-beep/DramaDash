package com.example.dramadash

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class PaymentsActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnMarkPaid: Button
    private lateinit var tvPaymentsTitle: TextView
    private lateinit var tvPaymentsContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        btnBack = findViewById(R.id.btnBack)
        btnMarkPaid = findViewById(R.id.btnMarkPaid)
        tvPaymentsTitle = findViewById(R.id.tvPaymentsTitle)
        tvPaymentsContent = findViewById(R.id.tvPaymentsContent)

        val prefs = getSharedPreferences("DramaDashPrefs", MODE_PRIVATE)
        val editor = prefs.edit()

        val studentName = prefs.getString("studentName", "User")
        val day = prefs.getString("selectedDay", "Not selected")
        val time = prefs.getString("selectedTime", "Not selected")
        val location = prefs.getString("selectedLocation", "Not selected")

        tvPaymentsTitle.text = "Payments for $studentName"

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        val fee = "£56"

        val dueDay = when (location) {
            "The Playhouse" -> 1
            "The Vale Centre" -> 5
            "The Gasyard" -> 10
            "The Waterside Shared Village" -> 15
            "Cumber" -> 1
            else -> 1
        }

        val savedMonth = prefs.getInt("paymentMonth", -1)
        val savedYear = prefs.getInt("paymentYear", -1)
        var status = prefs.getString("paymentStatus", null)

        if (savedMonth != currentMonth || savedYear != currentYear) {
            status = null
            editor.remove("paymentStatus")
            editor.remove("paymentMonth")
            editor.remove("paymentYear")
            editor.apply()
        }

        if (status == null) {
            status = when {
                today < dueDay -> "Due"
                today == dueDay -> "Due Today"
                else -> "Overdue"
            }
        }

        fun updateUI(status: String) {
            val fullText =
                "Student: $studentName\n" +
                        "Class: $day at $time\n" +
                        "Location: $location\n\n" +
                        "Monthly Fee: $fee\n" +
                        "Due Date: $dueDay of each month\n" +
                        "Status: $status"

            val spannable = SpannableString(fullText)

            val start = fullText.indexOf("Status:") + 8
            val end = start + status.length

            val color = when (status) {
                "Paid" -> android.graphics.Color.parseColor("#4CAF50")
                "Due", "Due Today" -> android.graphics.Color.parseColor("#FF9800")
                "Overdue" -> android.graphics.Color.parseColor("#F44336")
                else -> android.graphics.Color.BLACK
            }

            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            tvPaymentsContent.text = spannable

            btnMarkPaid.isEnabled = status != "Paid"
            btnMarkPaid.text = if (status == "Paid") "Payment Complete" else "Mark as Paid"
        }

        updateUI(status)

        btnMarkPaid.setOnClickListener {
            editor.putString("paymentStatus", "Paid")
            editor.putInt("paymentMonth", currentMonth)
            editor.putInt("paymentYear", currentYear)
            editor.apply()

            updateUI("Paid")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}