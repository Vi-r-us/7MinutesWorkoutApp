package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // val flStartButton: FrameLayout = findViewById(R.id.fl_Start)
        binding?.flStart?.setOnClickListener {

            // Dialog Builder "Are you ready?"
            val readyDialog = Dialog(this)
            readyDialog.setContentView(R.layout.are_you_ready_layout_dialog)
            readyDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            readyDialog.setTitle("Are you ready?")

            // on ready Start the exercise activity
            readyDialog.findViewById<Button>(R.id.ready_button).setOnClickListener {
                val intent = Intent(this, ExerciseActivity::class.java)
                startActivity(intent)
                readyDialog.dismiss()
            }
            // on cancel dismiss the dialog box
            readyDialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                readyDialog.dismiss()
            }

            readyDialog.show()
        }

        // Go to BMI Activity
        binding?.ibBmi?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        // Go to History Activity
        binding?.ibHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}