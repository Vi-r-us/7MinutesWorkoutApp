package com.example.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(private val items: ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

        class ViewHolder(binding: ItemExerciseStatusBinding):
                RecyclerView.ViewHolder(binding.root) {
                    val tvItem = binding.tvItem
                }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get the item currently at as model
        val model: ExerciseModel = items[position]
        // set the id as text of the model
        holder.tvItem.text = model.getId().toString()

        when {
            model.getIsSelected() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                    R.drawable.item_circular_thin_ring_background)
                holder.tvItem.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.white))
            }
            model.getIsCompleted() -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_circular_completed_background)
                holder.tvItem.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.white))
            }
            else -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(holder.itemView.context,
                        R.drawable.item_circular_color_grey_background)
                holder.tvItem.setTextColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.black))
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}