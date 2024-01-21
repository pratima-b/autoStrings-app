package com.example.autoinsight

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker

class WashContactActivity : AppCompatActivity() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washcontact)

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
                val intent = Intent(this, WashPersonalActivity::class.java).apply {
                    putExtra("mobile", j.text.toString())
                    putExtra("email", k.text.toString())
                }
                startActivity(intent)
            }
        }


        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }

}