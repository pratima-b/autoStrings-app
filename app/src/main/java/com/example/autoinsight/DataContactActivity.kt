package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker


class DataContactActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var a: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var b: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var c: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var d: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var e: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var f: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var g: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var h: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var i: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var j: EditText
        @SuppressLint("StaticFieldLeak")
        lateinit var k: EditText
        lateinit var l: EditText
        lateinit var m: EditText
    }

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
        setContentView(R.layout.activity_datacontact)

        val ccp: CountryCodePicker = this.findViewById(R.id.countryCodeHolder)
        j = this.findViewById(R.id.mobile)
        k = this.findViewById(R.id.email)

        ccp.registerCarrierNumberEditText(j)

        val verify = this.findViewById<Button>(R.id.verify)
        verify.setOnClickListener {
            if (j.text.toString().isEmpty() || k.text.toString().isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill the mandatory * field.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isValidEmail(k.text.toString())) {
                // Show an error message or handle the invalid email condition
                Toast.makeText(
                    applicationContext,
                    "Invalid email address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, DataPersonalActivity::class.java).apply {
                    putExtra("mobile", j.text.toString())
                    putExtra("email", k.text.toString())
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }


        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }

}