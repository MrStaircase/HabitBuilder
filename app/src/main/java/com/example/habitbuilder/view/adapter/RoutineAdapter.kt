package com.example.habitbuilder.view.adapter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.data.Routine
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.view.activity.RoutineActivity

class RoutineAdapter(
    private val routines: List<Routine>,
    private val context: Context
) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    class ViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = Button(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ViewHolder(button)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val routine = routines[position]
        holder.button.text = routine.name
        holder.button.setOnClickListener {
            val intent = Intent(context, RoutineActivity::class.java)
            intent.putExtra("routineId", routine.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = routines.size
}