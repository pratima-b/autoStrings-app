package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.autoinsight.WashContactActivity.Companion.a
import com.example.autoinsight.WashContactActivity.Companion.b
import com.example.autoinsight.WashContactActivity.Companion.c
import com.example.autoinsight.WashContactActivity.Companion.d
import com.example.autoinsight.WashContactActivity.Companion.e
import com.example.autoinsight.WashContactActivity.Companion.f
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class WashPersonalActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datapersonal)

       a = this.findViewById(R.id.firstName)
      b = this.findViewById(R.id.lastName)
      c = this.findViewById(R.id.houseNo)
        d = this.findViewById(R.id.city)
        e = this.findViewById(R.id.state)
     f = this.findViewById(R.id.pinCode)
        // Retrieve the mobile and email data from the intent
        val mobile = intent.getStringExtra("mobile")
        val email = intent.getStringExtra("email")

        val pnextButton = this.findViewById<Button>(R.id.pnextButton)

        pnextButton.setOnClickListener {
            if (a.text.toString().isEmpty() || b.text.toString().isEmpty() || c.text.toString()
                    .isEmpty() || d.text.toString().isEmpty() || e.text.toString()
                    .isEmpty() || f.text.toString().isEmpty()
            ) {
                Toast.makeText(applicationContext, "Please fill all the mandatory * fields.", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                val intent = Intent(this, WashCarActivity::class.java).apply {
                    // Pass the UI component data and mobile and email data to DataCarActivity
                    putExtra("firstName", a.text.toString())
                    putExtra("lastName", b.text.toString())
                    putExtra("houseNo", c.text.toString())
                    putExtra("city", d.text.toString())
                    putExtra("state", e.text.toString())
                    putExtra("pinCode",f.text.toString())
                    putExtra("mobile", mobile)
                    putExtra("email", email)
                }
                startActivity(intent)
            }
        }



        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
}