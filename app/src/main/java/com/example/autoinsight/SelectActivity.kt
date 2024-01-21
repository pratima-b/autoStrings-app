package com.example.autoinsight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val dataCollection = this.findViewById<Button>(R.id.dataCollectionBtn)
        dataCollection.setOnClickListener {
            val intent = Intent(this, DataContactActivity::class.java).apply {
            }
            startActivity(intent)
        }

        val carWashing = this.findViewById<Button>(R.id.carWashingBtn)
        carWashing.setOnClickListener {
            val intent = Intent(this, WashContactActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }
}