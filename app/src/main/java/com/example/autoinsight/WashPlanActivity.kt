/*package com.example.autoinsight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class WashPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washplan)

        val planList = resources.getStringArray(R.array.planList)
        val arrayAdapterPlan: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown, planList)
        val plan = findViewById<AutoCompleteTextView>(R.id.plan)
        plan.setAdapter(arrayAdapterPlan)

    }
}*/