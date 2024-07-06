package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class PaymentActivity : AppCompatActivity() {

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val button1 = this.findViewById<ImageButton>(R.id.button1)
        button1.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashContactActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button2 = this.findViewById<ImageButton>(R.id.button2)
        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashPersonalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button3 = this.findViewById<ImageButton>(R.id.button3)
        button3.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WashCarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val button4 = this.findViewById<ImageButton>(R.id.button4)
        button4.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, activity_washplans::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        })

        val services = resources.getStringArray(R.array.services)
        val arrayAdapterAns: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, services)
        val ans = findViewById<AutoCompleteTextView>(R.id.ans)
        ans.setAdapter(arrayAdapterAns)

        val submitButton = this.findViewById<Button>(R.id.submitButton)

        submitButton.visibility = View.GONE

        var selectedText = ""

        ans.setOnItemClickListener { parent, view, position, id ->
            selectedText = parent.getItemAtPosition(position).toString()
            if (selectedText == "Yes") {
                submitButton.visibility = View.VISIBLE
            } else {
                selectedText = "No"
                submitButton.visibility = View.GONE

            }
        }

        var obtainedPlan: String


        obtainedPlan = intent.getStringExtra("planSelected").toString()



        submitButton.setOnClickListener {
            // Display a toast message after clicking the submit button
            showToast("Data saved to Firestore successfully")
            val toastMessage = "Thankyou"
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            val intent= Intent(this,SelectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            // You can perform additional actions here if needed
        }


        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }



    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}