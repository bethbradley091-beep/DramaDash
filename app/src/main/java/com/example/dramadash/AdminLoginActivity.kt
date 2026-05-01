package com.example.dramadash

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etAdminUsername: EditText
    private lateinit var etAdminPassword: EditText
    private lateinit var btnAdminLogin: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        databaseHelper = DatabaseHelper(this)

        btnBack = findViewById(R.id.btnBack)
        etAdminUsername = findViewById(R.id.etAdminUsername)
        etAdminPassword = findViewById(R.id.etAdminPassword)
        btnAdminLogin = findViewById(R.id.btnAdminLogin)

        btnBack.setOnClickListener {
            finish()
        }

        btnAdminLogin.setOnClickListener {

            val username = etAdminUsername.text.toString().trim()
            val password = etAdminPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter admin username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = hashPassword(password)
            val validLogin = databaseHelper.checkLogin(username, hashedPassword)
            val role = databaseHelper.getUserRole(username, hashedPassword)

            if (validLogin && role != null && role == "admin") {
                Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid admin login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}