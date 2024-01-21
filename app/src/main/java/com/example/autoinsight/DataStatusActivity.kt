package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.autoinsight.DataContactActivity.Companion.a
import com.example.autoinsight.DataContactActivity.Companion.b
import com.example.autoinsight.DataContactActivity.Companion.c
import com.example.autoinsight.DataContactActivity.Companion.d
import com.example.autoinsight.DataContactActivity.Companion.e
import com.example.autoinsight.DataContactActivity.Companion.f
import com.example.autoinsight.DataContactActivity.Companion.g
import com.example.autoinsight.DataContactActivity.Companion.h
import com.example.autoinsight.DataContactActivity.Companion.i
import com.example.autoinsight.DataContactActivity.Companion.j
import com.example.autoinsight.DataContactActivity.Companion.k
import com.example.autoinsight.DataContactActivity.Companion.l
import com.example.autoinsight.DataContactActivity.Companion.m

class DataStatusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastatus)

        // Retrieve the values passed from DataCarActivity
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

        var selectedText: String? = null

        val services = resources.getStringArray(R.array.services)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown, services)
        val serviceDone = findViewById<AutoCompleteTextView>(R.id.serviceDone)
        serviceDone.setAdapter(arrayAdapter)

        val doneWhere = resources.getStringArray(R.array.doneWhere)
        val arrayAdapter1: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, doneWhere)
        val where = findViewById<AutoCompleteTextView>(R.id.where)
        where.setAdapter(arrayAdapter1)

        val scheduledWhen = resources.getStringArray(R.array.scheduledWhen)
        val arrayAdapter2: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, scheduledWhen)
        val whenScheduled = findViewById<AutoCompleteTextView>(R.id.whenScheduled)
        whenScheduled.setAdapter(arrayAdapter2)

        val doneOften = resources.getStringArray(R.array.doneWhere)
        val arrayAdapter3: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, doneOften)
        val often = findViewById<AutoCompleteTextView>(R.id.often)
        often.setAdapter(arrayAdapter3)

        val avg = resources.getStringArray(R.array.avg)
        val arrayAdapter4: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, avg)
        val avgRunning = findViewById<AutoCompleteTextView>(R.id.avgRunning)
        avgRunning.setAdapter(arrayAdapter4)

        val whenlayout = findViewById<LinearLayout>(R.id.whenlayout)
        val wherelayout = findViewById<LinearLayout>(R.id.wherelayout)

        serviceDone.setOnItemClickListener { _, _, position, _ ->
            selectedText = serviceDone.text.toString()
            if (selectedText == "Yes") {
                wherelayout.visibility = View.VISIBLE
                whenlayout.visibility = View.GONE
            } else {
                selectedText = "No"
                whenlayout.visibility = View.VISIBLE
                wherelayout.visibility = View.GONE
            }
        }

        fun isValidSelection(autoCompleteTextView: AutoCompleteTextView): Boolean {
            val selectedValue = autoCompleteTextView.text.toString()
            return selectedValue != "Select your answer"
        }
        val snext = this.findViewById<Button>(R.id.snext)
        snext.setOnClickListener {
            if (isValidSelection(serviceDone)) {
                if (selectedText == "Yes") {
                    if (isValidSelection(often) && isValidSelection(avgRunning) && isValidSelection(where)) {
                        val intent = Intent(this, DataFeedbackActivity::class.java).apply {
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
                            putExtra("serviceDone", selectedText)
                            putExtra("where", where.text.toString())
                            putExtra("often", often.text.toString())
                            putExtra("avgRunning", avgRunning.text.toString())
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Please fill all the mandatory * fields", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (isValidSelection(often) && isValidSelection(avgRunning) && isValidSelection(whenScheduled)) {
                        val intent = Intent(this, DataFeedbackActivity::class.java).apply {
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
                            putExtra("serviceDone", selectedText)
                            putExtra("often", often.text.toString())
                            putExtra("avgRunning", avgRunning.text.toString())
                            putExtra("whenScheduled", whenScheduled.text.toString())
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Please fill all the mandatory * fields", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all the mandatory * fields", Toast.LENGTH_SHORT).show()
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