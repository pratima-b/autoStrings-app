package com.example.autoinsight

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.net.ConnectivityManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class activity_washplans : AppCompatActivity() {
    private lateinit var myImageViews: Array<ImageView>
    private lateinit var myButtons: Array<Button>
    private val normalImageResources = arrayOf(
        R.drawable.plan1,
        R.drawable.plan2,
        R.drawable.plan3,
        R.drawable.plan4
    )
    private val clickedImageResources = arrayOf(
        R.drawable.clickedplan1,
        R.drawable.clickedplan2,
        R.drawable.clickedplan3,
        R.drawable.clickedplan4
    )
    private var isImageClicked = BooleanArray(4) { false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washplans)

        // Initialize arrays for ImageView and Button references
        myImageViews = arrayOf(
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4),
            findViewById(R.id.imageView5),
            findViewById(R.id.imageView6)
        )
        myButtons = arrayOf(
            findViewById(R.id.button1),
            findViewById(R.id.button7),
            findViewById(R.id.button6),
            findViewById(R.id.button5)
        )

        var proceedButton = findViewById<Button>(R.id.proceedbtn)

        // Set click listeners for each button and its corresponding ImageView
        for (i in myButtons.indices) {
            val imageView = myImageViews[i]
            val normalImage = normalImageResources[i]
            val clickedImage = clickedImageResources[i]

            myButtons[i].setOnClickListener(View.OnClickListener {
                if (isImageClicked[i]) {
                    // If the same button is clicked again, return it to the initial state
                    isImageClicked[i] = false
                    imageView.setImageResource(normalImage)
                    myButtons[i].setBackgroundResource(R.drawable.round_btn)
                } else {
                    // Reset the state of all other buttons and images
                    resetButtonsAndImages(i)

                    // Change the image in the corresponding ImageView
                    isImageClicked[i] = true
                    imageView.setImageResource(clickedImage)

                    // Change the Button color based on the flag
                    myButtons[i].setBackgroundResource(R.drawable.btnblue)
                }

                when (i) {
                    0 -> showToast("Plan 499 selected")
                    1 -> showToast("Plan 699 selected")
                    2 -> showToast("Plan 799 selected")
                    3 -> showToast("Plan 999 selected")
                }
            })
        }

        proceedButton.setOnClickListener {
            // Check if the internet is available
            if (isNetworkAvailable()) {
                // Extract the selected plan
                var selectedPlan = ""
                for (i in myButtons.indices) {
                    if (isImageClicked[i]) {
                        when (i) {
                            0 -> selectedPlan = "499"
                            1 -> selectedPlan = "699"
                            2 -> selectedPlan = "799"
                            3 -> selectedPlan = "999"
                        }
                    }
                }

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

                        val intent = Intent(this, PaymentActivity::class.java)
                        intent.putExtra("planSelected", selectedPlan)
                        startActivity(intent)
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
        }


    }

    private fun resetButtonsAndImages(excludeIndex: Int) {
        for (i in myButtons.indices) {
            if (i != excludeIndex) {
                isImageClicked[i] = false
                myImageViews[i].setImageResource(normalImageResources[i])
                myButtons[i].setBackgroundResource(R.drawable.round_btn)
            }
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
