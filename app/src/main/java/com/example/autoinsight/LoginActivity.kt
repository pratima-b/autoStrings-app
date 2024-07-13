package com.example.autoinsight

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale


class LoginActivity : AppCompatActivity() {

    var firstPressTime: Long = 0

    override fun onBackPressed() {
        if (firstPressTime + 2000 > System.currentTimeMillis()) {
            finishAffinity()  // This will close all activities and exit the app
        } else {
            Toast.makeText(baseContext, "Press Back again to Exit", Toast.LENGTH_SHORT).show()
        }
        firstPressTime = System.currentTimeMillis()
    }

    private lateinit var passwordEditText: EditText
    private lateinit var showHide: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        showHide = findViewById<ImageView>(R.id.showHide) //
        val emailEditText = findViewById<EditText>(R.id.firstName) // Assuming this is the email input field

        passwordEditText = this.findViewById(R.id.passWord)

        showHide.setOnClickListener{
            togglePasswordVisibility()
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both Employee Id and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun togglePasswordVisibility() {
        if (passwordEditText.inputType == (TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showHide.setImageResource(R.drawable.ic_show_eye)
        } else {
            passwordEditText.inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            showHide.setImageResource(R.drawable.ic_hide_eye)
        }
    }

    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        val loweremail=email?.toLowerCase(Locale.ROOT)
        val emailusername= "$loweremail@autostrings.com"
        auth.signInWithEmailAndPassword(emailusername, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login was successful
                    val user = auth.currentUser
                    Toast.makeText(this, loweremail.toString()+" Login successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SelectActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please Enter Valid Employee ID or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
