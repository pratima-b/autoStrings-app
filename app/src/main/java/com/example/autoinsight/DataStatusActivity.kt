package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
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
import com.google.firebase.auth.FirebaseAuth

class DataStatusActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_datastatus)


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
        val snext = this.findViewById<Button>(R.id.nextbtn)
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
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    } else {
                        Toast.makeText(this, "Please fill all the mandatory * fields", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all the mandatory * fields", Toast.LENGTH_SHORT).show()
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