package com.example.autoinsight

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.net.ConnectivityManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide

class activity_washplans : AppCompatActivity() {

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washplans)


        val fetchedImageView = findViewById<ImageView>(R.id.fetched)

        // Fetch and display the image from Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference.child("plans.png")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(fetchedImageView)
        }.addOnFailureListener {
            showToast("Failed to load image")
        }


        val button1 = this.findViewById<ImageButton>(R.id.button1)
        button1.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashContactActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button2 = this.findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashPersonalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button3 = this.findViewById<ImageButton>(R.id.button3)
        button3.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashCarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val proceedButton = findViewById<Button>(R.id.proceedbtn)
        val selectedPlanEditText = findViewById<EditText>(R.id.lastNameuser)
        val paymentStatusDropdown = findViewById<AutoCompleteTextView>(R.id.ans2)

        // Setup the dropdown menu
        val paymentStatusOptions = arrayOf("Yes", "No")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, paymentStatusOptions)
        paymentStatusDropdown.setAdapter(adapter)

        proceedButton.setOnClickListener {
            if (isNetworkAvailable()) {
                val selectedPlan = selectedPlanEditText.text.toString()
                val paymentStatus = paymentStatusDropdown.text.toString()


                // Extract the intented data received from the previous activity
                val intent = intent
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

                // Store the data in Firestore
                val db = FirebaseFirestore.getInstance()
                val currentUserEmail = email // Change this to the user's email
                val docRef = db.collection("carWashing").document(email.toString())
                val userEmail = currentUserEmail

                val data = hashMapOf(
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
                    "selectedPlan" to selectedPlan
                )

                docRef.set(data)
                    .addOnSuccessListener {

                        val intent = Intent(this, SelectActivity::class.java)
                        intent.putExtra("planSelected", selectedPlan)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    }
                    .addOnFailureListener {
                        showToast("Failed to save data to Firestore")
                    }
            } else {
                // Show a dialog or your custom message when internet is not available
                showNoInternetDialog()
            }
        }



        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }


    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showNoInternetDialog() {
        val dialog = CustomDialog(this, "Please turn on the internet connection")
        dialog.show()
    }
}
