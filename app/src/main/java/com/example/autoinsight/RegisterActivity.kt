package com.example.autoinsight

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    @Deprecated("Deprecated in Java")
    var firstPressTime: Long = 0

    override fun onBackPressed() {
        if (firstPressTime + 2000 > System.currentTimeMillis()) {
            finishAffinity()  // This will close all activities and exit the app
        } else {
            Toast.makeText(baseContext, "Press Back again to Exit", Toast.LENGTH_SHORT).show()
        }
        firstPressTime = System.currentTimeMillis()
    }

    private lateinit var regpasswordEditText: EditText
    private lateinit var showHide: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        showHide = findViewById(R.id.showHide)
        regpasswordEditText = this.findViewById(R.id.regpassWord)

        showHide.setOnClickListener{
            togglePasswordVisibility()
        }

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.regpassWord)
        val firstNameEditText = findViewById<EditText>(R.id.firstNameuser)
        val lastNameEditText = findViewById<EditText>(R.id.lastNameuser)
        val mobileNumberEditText = findViewById<EditText>(R.id.regmob)
        val registerButton = findViewById<Button>(R.id.registerBtn)
        val generateButton = findViewById<Button>(R.id.generateButton)
        val empidUI = findViewById<TextView>(R.id.empid)
        var empId: String? = null

        generateButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            // Generate employee ID
            empId = generateEmployeeId(firstName, lastName)

            // Set the generated empId to the "empid" TextView
            empidUI.text = empId
        }

        registerButton.setOnClickListener {
            // Get user input
            val username = empId?.toLowerCase(Locale.ROOT)
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val mobileNumber = mobileNumberEditText.text.toString()

            // Check if any field is empty
            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || mobileNumber.isEmpty() || empId.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill the mandatory * field.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if email is valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if mobile number is valid
            if (!mobileNumber.matches(Regex("^\\d{10}$"))) {
                Toast.makeText(this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register the user with email and password
            auth.createUserWithEmailAndPassword("$username@autostrings.com", password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration successful
                        val user = auth.currentUser
                        if (user != null) {
                            // Create a user object with data
                            val userData = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "mobileNumber" to mobileNumber,
                                "employeeId" to empId,
                                "emaildId" to email
                            )

                            // Store user data in Firestore
                            db.collection("users")
                                .document(empId.toString())
                                .set(userData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "User data added to Firestore")
                                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginActivity::class.java).apply {
                                    }
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                                    // Redirect to the next activity or perform other actions
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding user data to Firestore", e)
                                    // Handle error
                                }
                        }
                    } else {
                        // Registration failed
                        Log.w(TAG, "User registration failed", task.exception)
                        // Handle registration failure
                    }
                }
        }
    }

    private fun generateEmployeeId(firstName: String, lastName: String): String {
        // Generate a random 3-digit code
        val randomCode = (100..999).random()

        // Create an employee ID using the first 2 letters of the first name,
        // the last 2 letters of the last name, and the 3-digit code
        val empId = firstName.take(2).toUpperCase(Locale.ROOT) +
                lastName.takeLast(2).toUpperCase(Locale.ROOT) +
                randomCode.toString()

        return empId
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private fun togglePasswordVisibility() {
        if (regpasswordEditText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            regpasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showHide.setImageResource(R.drawable.ic_show_eye)
        } else {
            regpasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            showHide.setImageResource(R.drawable.ic_hide_eye)
        }
    }
}
