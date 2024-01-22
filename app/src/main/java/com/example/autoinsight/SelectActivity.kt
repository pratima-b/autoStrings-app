package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SelectActivity : AppCompatActivity() {

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

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

        val logout = this.findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }
    }
}