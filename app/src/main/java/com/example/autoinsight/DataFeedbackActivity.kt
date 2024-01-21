package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DataFeedbackActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datafeedback)

        val services = resources.getStringArray(R.array.services)
        val arrayAdapterAns: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, services)
        val ans = findViewById<AutoCompleteTextView>(R.id.ans)
        ans.setAdapter(arrayAdapterAns)

        val fnameyes = findViewById<EditText>(R.id.fnameyes)
        val lnameyes = findViewById<EditText>(R.id.lnameyes)

        fnameyes.visibility = View.GONE
        lnameyes.visibility = View.GONE

        val edit1 = findViewById<EditText>(R.id.edit1)
        val edit2 = findViewById<EditText>(R.id.edit2)

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
        val serviceDone = intent.getStringExtra("serviceDone")
        val often = intent.getStringExtra("often")
        val avgRunning = intent.getStringExtra("avgRunning")

        val fnext = this.findViewById<Button>(R.id.submit)

        var selectedText = ""

        ans.setOnItemClickListener { parent, view, position, id ->
            selectedText = parent.getItemAtPosition(position).toString()
            if (selectedText == "Yes") {
                fnameyes.visibility = View.VISIBLE
                lnameyes.visibility = View.VISIBLE
            } else {
                selectedText = "No"
                fnameyes.visibility = View.GONE
                lnameyes.visibility = View.GONE
            }
        }

        fnext.setOnClickListener {
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
                "serviceDone" to serviceDone,
                "often" to often,
                "avgRunning" to avgRunning,
                "answer" to selectedText, // Store selectedText
                "fNameYes" to if (selectedText == "Yes") fnameyes.text.toString() else "",
                "lNameYes" to if (selectedText == "Yes") lnameyes.text.toString() else "",
                "Reasons behind selecting service center" to edit1.text.toString(),
                "edit2" to edit2.text.toString()
            )

            // Add the user data (HashMap) to Firestore
            currentUserDoc.set(userData)
                .addOnSuccessListener {
                    // Data saved successfully
                    Toast.makeText(this, "Data saved to Firestore", Toast.LENGTH_SHORT).show()
                    // Navigate to SelectActivity
                    val intent = Intent(this, SelectActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    // Handle the error
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }


        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }


    }
}
