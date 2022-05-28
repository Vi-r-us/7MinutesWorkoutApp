package com.example.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import com.example.a7minutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<HistoryEntity>,
                     private val deleteListener: (id:Int)->Unit)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding): RecyclerView.ViewHolder(binding.root) {
        val tvSNumber = binding.tvSNumber
        val tvExerciseDone = binding.tvExerciseDone
        val tvDate = binding.tvDate
        val ibDelete = binding.ibDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvSNumber.text = (position+1).toString()
        holder.tvExerciseDone.text = "${item.totalExerciseDone} Exercise Done"
        holder.tvDate.text = item.date

        holder.ibDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}