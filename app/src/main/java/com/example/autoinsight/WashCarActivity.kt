package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.autoinsight.WashContactActivity.Companion.a
import com.example.autoinsight.WashContactActivity.Companion.b
import com.example.autoinsight.WashContactActivity.Companion.c
import com.example.autoinsight.WashContactActivity.Companion.d
import com.example.autoinsight.WashContactActivity.Companion.e
import com.example.autoinsight.WashContactActivity.Companion.f
import com.google.firebase.auth.FirebaseAuth

class WashCarActivity : AppCompatActivity() {

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



    private val brands = arrayOf(
        "Tata", "Maruti", "Mahindra & Mahindra", "Hyundai",
        "Toyota", "Volkswagen", "Honda", "GM/Chevrolet",
        "BMW", "Mercedes"
    )

    private val modelsMap = mapOf(
        "Tata" to arrayOf("Indica", "Tiago", "Tigor", "Harrier", "Safari","Nexon","Altroz","Punch","Zest","Nano","Sierra","Hexa","Aria","Sumo","Estate"),
        "Maruti" to arrayOf("Swift", "Dzire", "Ertiga", "Baleno", "Wagon R", "Alto","Presso","Versa","Eeco","Brezza","Celerio","Ciaz","Ignis","S Cross","XL6","Jumny","Vitara","Invicto","Cresent","Fronx"),
        "Mahindra & Mahindra" to arrayOf("Scorpio", "Bolero", "XUV500", "KUV100", "XUV300","XUV700","Thar", "Xylo"),
        "Hyundai" to arrayOf("i10", "i20", "Eon", "Xcent", "Venue", "Tucson", "Creta", "Grand i10", "Accent", "Sonata", "Verna", "Elentra", "Aura", "Alcazar",),
        "Toyota" to arrayOf("Camry", "Qualis", "Innova - Crysta", "Innova", "Corolla"),
        "Volkswagen" to arrayOf("Jetta", "Polo", "Atlas", "Golf", "Touareg", "Tiguan"),
        "Honda" to arrayOf( "Amaze", "City", "Civic", "CR-V", "Accord", "Jazz"),
        "GM/Chevrolet" to arrayOf("Beat", "Tavera", "Captiva", "Cruze"),
        "BMW" to arrayOf("X1","X3","X5","X7"),
        "Mercedes" to arrayOf("C-Class", "GLA", "S-Class", "E-Class", "A-Class", "GLE", "GLC", "GLS", "G-Class"),

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washcar)

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
            val intent = Intent(this, WashContactActivity::class.java)
            startActivity(intent)
        })

        val button2 = this.findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashPersonalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        /*g = this.findViewById(R.id.manf)
        h = this.findViewById(R.id.model)
        i = this.findViewById(R.id.manfYear)
        l = this.findViewById(R.id.regNo)
        m = this.findViewById(R.id.fuel)*/

        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val houseNo = intent.getStringExtra("houseNo")
        val city = intent.getStringExtra("city")
        val state = intent.getStringExtra("state")
        val pinCode = intent.getStringExtra("pinCode")
        val mobile = intent.getStringExtra("mobile")
        val email = intent.getStringExtra("email")



        val brandAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.manufacturer)
        val modelAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.car_model)

        // Populate the brand AutoCompleteTextView
        val brandAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, brands)
        brandAutoCompleteTextView.setAdapter(brandAdapter)

        // Set an item click listener for the brand AutoCompleteTextView
        brandAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedBrand = brands[position]
            val modelsForBrand = modelsMap[selectedBrand] ?: emptyArray()

            // Populate the model AutoCompleteTextView with models for the selected brand
            val modelAdapter =
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, modelsForBrand)
            modelAutoCompleteTextView.setAdapter(modelAdapter)


            val fuelType = resources.getStringArray(R.array.fuelType)
            val arrayAdapterFuel: ArrayAdapter<String> =
                ArrayAdapter(this, R.layout.dropdown, fuelType)
            val fuel = findViewById<AutoCompleteTextView>(R.id.fuel)
            fuel.setAdapter(arrayAdapterFuel)

            val carSegment = resources.getStringArray(R.array.carSegment)
            val arrayAdapterSegment: ArrayAdapter<String> =
                ArrayAdapter(this, R.layout.dropdown, carSegment)
            val segment = findViewById<AutoCompleteTextView>(R.id.segment)
            segment.setAdapter(arrayAdapterSegment)


            val cnextButton = this.findViewById<Button>(R.id.cnextButton)
            cnextButton.setOnClickListener {
                val manufacturer = brandAutoCompleteTextView.text.toString()
                val carModel = modelAutoCompleteTextView.text.toString()
                val fuelType = fuel.text.toString()
                val carSegment = segment.text.toString()

                // Create an intent to start the next activity and pass data as extras
                val intent = Intent(this, activity_washplans::class.java).apply {
                    putExtra("firstName", firstName)
                    putExtra("lastName", lastName)
                    putExtra("houseNo", houseNo)
                    putExtra("city", city)
                    putExtra("state", state)
                    putExtra("pinCode", pinCode)
                    putExtra("mobile", mobile)
                    putExtra("email", email)
                    putExtra("manufacturer", manufacturer)
                    putExtra("carModel", carModel)
                    putExtra("fuelType", fuelType)
                    putExtra("carSegment", carSegment)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

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