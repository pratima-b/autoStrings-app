// CustomDialog.kt

package com.example.autoinsight

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView

class CustomDialog(context: Context, private val message: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_layout)

        val messageTextView: TextView = findViewById(R.id.messageTextView)
        val closeButton: Button = findViewById(R.id.closeButton)

        messageTextView.text = message

        closeButton.setOnClickListener {
            dismiss()
        }
    }
}
