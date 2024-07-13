package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DataFeedbackActivity : AppCompatActivity() {
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datafeedback)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val logout = findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            logoutUser()
        }

        val homeImageView = findViewById<ImageView>(R.id.home)
        homeImageView.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        val button1 = this.findViewById<ImageButton>(R.id.button1)
        button1.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DataContactActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)


        })

        val button2 = this.findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DataPersonalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button3 = this.findViewById<ImageButton>(R.id.button3)
        button3.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DataCarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button4 = this.findViewById<ImageButton>(R.id.button4)
        button4.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DataStatusActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val services = resources.getStringArray(R.array.services)
        val arrayAdapterAns: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, services)
        val ans = findViewById<AutoCompleteTextView>(R.id.ans)
        ans.setAdapter(arrayAdapterAns)

        val ref1 = this.findViewById<EditText>(R.id.fnameyes)
        val ref2 = this.findViewById<EditText>(R.id.lnameyes)
        val contact1 = this.findViewById<EditText>(R.id.contactNum1)
        val contact2 = this.findViewById<EditText>(R.id.contactNum2)
        val edit1 = this.findViewById<EditText>(R.id.edit1)
        val edit2 = this.findViewById<EditText>(R.id.edit2)


        ref1.visibility = View.GONE
        ref2.visibility = View.GONE
        contact1.visibility = View.GONE
        contact2.visibility = View.GONE

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            // Handle the case where the user is not logged in or authentication fails
            // You may want to redirect the user to the login screen or perform some other action.
            return
        }

        val userEmail = currentUser.email

        // Retrieve the values passed from DataStatusActivity
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val houseNo = intent.getStringExtra("houseNo")
        val city = intent.getStringExtra("city")
        val state = intent.getStringExtra("state")
        val pinCode = intent.getStringExtra("pinCode")
        val mobile = intent.getStringExtra("mobile")
        val email = intent.getStringExtra("email")
        val manufacturer = intent.getStringExtra("manufacturer")
        val carModel = intent.getStringExtra("carModel")
        val fuelType = intent.getStringExtra("fuelType")
        val carSegment = intent.getStringExtra("carSegment")
        val manufacturingYear = intent.getStringExtra("manufacturingYear")
        val registrationNumber = intent.getStringExtra("registrationNumber")
        val serviceDone = intent.getStringExtra("serviceDone")
        val often = intent.getStringExtra("often")
        val avgRunning = intent.getStringExtra("avgRunning")
        val totalRunning = intent.getStringExtra("totalRunning")
        val where = intent.getStringExtra("where")
        val whenScheduled = intent.getStringExtra("whenScheduled")

        val fnext = this.findViewById<Button>(R.id.submit)

        var selectedText = ""

        ans.setOnItemClickListener { parent, view, position, id ->
            selectedText = parent.getItemAtPosition(position).toString()
            if (selectedText == "Yes") {
                ref1.visibility = View.VISIBLE
                ref2.visibility = View.VISIBLE
                contact1.visibility = View.VISIBLE
                contact2.visibility = View.VISIBLE

            } else {
                selectedText = "No"
                ref1.visibility = View.GONE
                ref2.visibility = View.GONE
                contact1.visibility = View.GONE
                contact2.visibility = View.GONE
            }
        }

        fnext.setOnClickListener {
            // Validate all fields before submitting
            if (selectedText.isEmpty() || edit1.text.toString().isEmpty() || edit2.text.toString().isEmpty() ||
                (selectedText == "Yes" && (ref1.text.toString().isEmpty() || ref2.text.toString().isEmpty() || contact1.text.toString().isEmpty() || contact2.text.toString().isEmpty()))
            ) {
                Toast.makeText(this, "Fill all the mandatory fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate contact numbers
            if (selectedText == "Yes" && (contact1.text.toString().length != 10 || contact2.text.toString().length != 10)) {
                Toast.makeText(this, "Phone number must be of 10 digits only", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firestore instance
            val db = FirebaseFirestore.getInstance()

            // Firestore collection reference
            val dataCollection = db.collection("datacollection")
            val currentUserDoc = dataCollection.document(email.toString() ?: "unknown_email")

            // Create a HashMap to store all the data
            val userData = hashMapOf(
                "collectedBy" to userEmail,
                "firstName" to firstName,
                "lastName" to lastName,
                "houseNo" to houseNo,
                "city" to city,
                "state" to state,
                "pinCode" to pinCode,
                "mobile" to mobile,
                "email" to email,
                "manufacturer" to manufacturer,
                "carModel" to carModel,
                "fuelType" to fuelType,
                "carSegment" to carSegment,
                "registrationNumber" to registrationNumber,
                "manufacturingYear" to manufacturingYear,
                "serviceDone" to serviceDone,
                "often" to often,
                "avgRunning" to avgRunning,
                "totalRunning" to totalRunning,
                "where" to where,
                "whenScheduled" to whenScheduled,
                "answer" to selectedText, // Store selectedText
                "ref1" to if (selectedText == "Yes") ref1.text.toString() else "",
                "ref2" to if (selectedText == "Yes") ref2.text.toString() else "",
                "contactNumber1" to if (selectedText == "Yes") contact1.text.toString() else "",
                "contactNumber2" to if (selectedText == "Yes") contact2.text.toString() else "",
                "Reasons behind selecting service center" to edit1.text.toString(),
                "Will you choose Autostrings if we provide you better quality service at an affordable price?" to edit2.text.toString()
            )

            Log.d("Debug", "userData: $userData")

            // Add the user data (HashMap) to Firestore
            currentUserDoc.set(userData)
                .addOnSuccessListener {
                    // Data saved successfully
                    Toast.makeText(this, "Data submitted", Toast.LENGTH_SHORT).show()
                    // Navigate to SelectActivity
                    val intent = Intent(this, SelectActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                }
                .addOnFailureListener {
                    // Handle the error
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }



    }

    private fun logoutUser() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish() // Close the current activity
    }
}
