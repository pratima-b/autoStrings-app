package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class SelectActivity : AppCompatActivity() {



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
        setContentView(R.layout.activity_select)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        val logout = findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            logoutUser()
        }

        val homeImageView = findViewById<ImageView>(R.id.home)
        homeImageView.setOnClickListener {
            Toast.makeText(this, "This is Home Page", Toast.LENGTH_SHORT).show()
        }


        val dataCollection = this.findViewById<Button>(R.id.dataCollectionBtn)
        dataCollection.setOnClickListener {
            val intent = Intent(this, DataContactActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val carWashing = this.findViewById<Button>(R.id.carWashingBtn)
        carWashing.setOnClickListener {
            val intent = Intent(this, WashContactActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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