package com.example.habitbuilder.view.adapter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.view.activity.ActionActivity

class ActionAdapter(
    private val actions: List<ActionEntity>,
    private val context: Context
) : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    class ViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val button = Button(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
        return ViewHolder(button)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actions[position]
        holder.button.text = action.description
        holder.button.setOnClickListener {
            val intent = Intent(context, ActionActivity::class.java).apply {
                putExtra("actionId", action.id)
                putExtra("routineId", action.routineId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = actions.size
}