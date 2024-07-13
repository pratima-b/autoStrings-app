package com.example.autoinsight

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.Dialog
import android.graphics.Rect
import android.net.ConnectivityManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
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
import com.google.firebase.auth.FirebaseAuth

class activity_washplans : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washplans)

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

        val fetchedImageView = findViewById<ImageView>(R.id.fetched)
        val zoomButton = findViewById<ImageButton>(R.id.zoomButton)

        // Fetch and display the image from Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference.child("plans.png")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(fetchedImageView)
        }.addOnFailureListener {
            showToast("Failed to load image")
        }
        zoomButton.setOnClickListener {
            openZoomDialog()
        }

        val button1 = findViewById<ImageButton>(R.id.button1)
        button1.setOnClickListener {
            val intent = Intent(this, WashContactActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val button2 = findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this, WashPersonalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val button3 = findViewById<ImageButton>(R.id.button3)
        button3.setOnClickListener {
            val intent = Intent(this, WashCarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

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

                // Extract the intended data received from the previous activity
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
                val docRef = db.collection("carWashing").document(email.toString())

                val data = hashMapOf(
                    "collectedBy" to email,
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
                    "selectedPlan" to selectedPlan,
                    "paymentStatus" to paymentStatus
                )

                docRef.set(data)
                    .addOnSuccessListener {
                        val intent = Intent(this, SelectActivity::class.java)
                        Toast.makeText(this, "Data submitted", Toast.LENGTH_SHORT).show()

                        intent.putExtra("planSelected", selectedPlan)
                        intent.putExtra("paymentStatus", paymentStatus)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                    .addOnFailureListener {
                        showToast("Failed to save the data")
                    }
            } else {
                showNoInternetDialog()
            }
        }
    }


    private fun openZoomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_zoom_image)

        val zoomedImageView = dialog.findViewById<ImageView>(R.id.zoomedImageView)
        val storageReference = FirebaseStorage.getInstance().reference.child("plans.png")

        // Load the zoomed image into zoomedImageView
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .override(1080, 800)  // Adjust size as needed
                .into(zoomedImageView)
        }.addOnFailureListener {
            showToast("Failed to load zoomed image")
        }

        var scaleFactor = 1.0f
        var lastFocusX = 0f
        var lastFocusY = 0f
        var focusX = 0f
        var focusY = 0f

        val scaleDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 10.0f))  // Adjust boundaries as necessary
                zoomedImageView.scaleX = scaleFactor
                zoomedImageView.scaleY = scaleFactor
                adjustTranslation(zoomedImageView, focusX, focusY)
                return true
            }
        })

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                focusX -= distanceX
                focusY -= distanceY
                adjustTranslation(zoomedImageView, focusX, focusY)
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
        })

        zoomedImageView.setOnTouchListener { _, event ->
            scaleDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastFocusX = event.x - focusX
                    lastFocusY = event.y - focusY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!scaleDetector.isInProgress) {
                        focusX = event.x - lastFocusX
                        focusY = event.y - lastFocusY
                        adjustTranslation(zoomedImageView, focusX, focusY)
                    }
                }
            }

            true
        }

        dialog.show()
    }

    private fun adjustTranslation(view: ImageView, focusX: Float, focusY: Float) {
        val parent = view.parent as View
        val parentWidth = parent.width
        val parentHeight = parent.height

        val viewWidth = view.width * view.scaleX
        val viewHeight = view.height * view.scaleY

        val maxTranslateX = (viewWidth - parentWidth) / 2
        val maxTranslateY = (viewHeight - parentHeight) / 2

        val constrainedX = focusX.coerceIn(-maxTranslateX, maxTranslateX)
        val constrainedY = focusY.coerceIn(-maxTranslateY, maxTranslateY)

        view.translationX = constrainedX
        view.translationY = constrainedY
    }



    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
