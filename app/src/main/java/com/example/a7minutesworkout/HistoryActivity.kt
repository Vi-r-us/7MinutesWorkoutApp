package com.example.a7minutesworkout

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistory)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }
        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkoutApp).db.historyDao()
        getAllHistory(dao)

        binding?.ibDeleteAll?.setOnClickListener {
            deleteAllRecordAlertDialog(dao)
        }

    }

    private fun getAllHistory(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllHistory().collect { allCompletedExercisesList ->
                if (allCompletedExercisesList.isNotEmpty()) {
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.ivNoDataAvailable?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    val dataToAdapter = ArrayList(allCompletedExercisesList)

                    val historyAdapter = HistoryAdapter(dataToAdapter,
                        { deleteId ->
                            deleteRecord(deleteId, historyDao) } )

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    binding?.rvHistory?.adapter = historyAdapter

                } else {
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.ivNoDataAvailable?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun deleteRecord(id: Int, historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.delete(HistoryEntity(id))
        }
    }

    private fun deleteAllRecordAlertDialog(historyDao: HistoryDao){
        // Dialog Builder "Delete All History"
        val sureDialog = Dialog(this)

        sureDialog.setContentView(R.layout.want_to_delete_dialog)
        sureDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        sureDialog.setCancelable(false)
        sureDialog.setTitle("Delete History")

        // on yes it will delete all the histories
        sureDialog.findViewById<Button>(R.id.yes_button).setOnClickListener {
            lifecycleScope.launch {
                historyDao.deleteAllHistory()

                Toast.makeText(applicationContext,
                    "All Record Deleted",
                    Toast.LENGTH_LONG)
                    .show()
            }
            sureDialog.dismiss()
        }
        // on no it will do nothing
        sureDialog.findViewById<Button>(R.id.no_button).setOnClickListener {
            sureDialog.dismiss()
        }

        sureDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}