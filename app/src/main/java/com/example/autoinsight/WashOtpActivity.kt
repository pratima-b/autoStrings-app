package com.example.autoinsight

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView

class WashOtpActivity : AppCompatActivity() {
    private lateinit var resendTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    var resendEnabled: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washotp)


        val mobile = intent.getStringExtra("mobile")
        val email = intent.getStringExtra("email")

//        Toast.makeText(this, mobile.toString(), Toast.LENGTH_SHORT).show()
        val verify = this.findViewById<Button>(R.id.verify)
        verify.setOnClickListener {
            val intent = Intent(this, WashPersonalActivity::class.java).apply {
                putExtra("mobile", mobile) // Pass the mobile data to DataPersonalActivity
                putExtra("email", email)   // Pass the email data to DataPersonalActivity
            }
            startActivity(intent)
        }



        resendTextView = findViewById(R.id.resendTextView)
        startCountdownTimer()

    }
    private fun startCountdownTimer() {

        resendEnabled = false
        resendTextView.setTextColor(Color.parseColor("#99000000"))

        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                resendTextView.setText("Resend OTP ("+secondsLeft+")")
            }

            override fun onFinish() {
                resendEnabled = true
                resendTextView.setText("Resend OTP")
                resendTextView.setTextColor(resources.getColor(R.color.blue))
            }
        }
        countDownTimer.start()
    }
}