package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WorkoutCompletionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_completion)

        val dao = (application as WorkoutApp).db.historyDao()
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            insertEntryToDatabase(dao)
            Toast.makeText(this, "Progress Saved", Toast.LENGTH_LONG).show()
            finish()
        }
        findViewById<Button>(R.id.btn_finish).setOnClickListener {
            finish()
        }
    }

    private fun insertEntryToDatabase(historyDao: HistoryDao) {

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date : ", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date : ", "" + date)

        val totalExerciseDone = intent.getIntExtra("totalExerciseDone", 0)
        Log.e("Total Exercise Done : ", "" + totalExerciseDone)

        lifecycleScope.launch{
            historyDao.insert(HistoryEntity(date = date, totalExerciseDone = totalExerciseDone))
        }
    }
}