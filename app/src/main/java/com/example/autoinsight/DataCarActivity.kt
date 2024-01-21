package com.example.autoinsight

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.autoinsight.DataContactActivity.Companion.g
import com.example.autoinsight.DataContactActivity.Companion.h
import com.example.autoinsight.DataContactActivity.Companion.i
import com.example.autoinsight.DataContactActivity.Companion.l
import com.example.autoinsight.DataContactActivity.Companion.m



class DataCarActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_datacar)

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
                val intent = Intent(this, DataStatusActivity::class.java).apply {
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
            }

            val logout = this.findViewById<ImageView>(R.id.logout)
            logout.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java).apply {
                }
                startActivity(intent)
            }
        }
    }
}