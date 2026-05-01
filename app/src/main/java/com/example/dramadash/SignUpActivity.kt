package com.example.dramadash

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etStudentName: EditText
    private lateinit var etParentName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var spinnerGroup: Spinner
    private lateinit var btnSignUp: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        databaseHelper = DatabaseHelper(this)

        btnBack = findViewById(R.id.btnBack)
        etStudentName = findViewById(R.id.etStudentName)
        etParentName = findViewById(R.id.etParentName)
        etUsername = findViewById(R.id.etSignUpUsername)
        etPassword = findViewById(R.id.etSignUpPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        spinnerGroup = findViewById(R.id.spinnerGroup)
        btnSignUp = findViewById(R.id.btnSignUp)

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

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        btnSignUp.setOnClickListener {
            val studentName = etStudentName.text.toString().trim()
            val parentName = etParentName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val groupName = spinnerGroup.selectedItem.toString()

            if (studentName.isEmpty() || parentName.isEmpty() || username.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()

            } else if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()

            } else if (!password.any { it.isDigit() }) {
                Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_SHORT).show()

            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

            } else if (databaseHelper.checkUsername(username)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()

            } else {
                val hashedPassword = hashPassword(password)

                val inserted = databaseHelper.insertUser(
                    studentName,
                    parentName,
                    username,
                    hashedPassword,
                    "parent",
                    groupName
                )

                if (inserted) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, SelectStudentActivity::class.java)
                    intent.putExtra("studentName", studentName)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}