package com.example.habitbuilder.view.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.view.adapter.ActionAdapter
import com.example.habitbuilder.R
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.viewmodel.RoutineViewModel
import java.util.Calendar

class RoutineActivity : ComponentActivity() {
    val viewModel: RoutineViewModel by viewModels()
    private var routineId: Int = -1
    private lateinit var tvRoutineTitle: TextView
    private lateinit var edRoutineName: EditText
    private lateinit var buttonContainer: RecyclerView
    private lateinit var btnNewAction: Button
    private lateinit var btnCalendar: Button
    private lateinit var btnTime: Button
    private lateinit var btnSaveRoutine: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)

        routineId = intent.getIntExtra("routineId", -1)

        loadViews()
        loadObservers()
        loadActions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRoutine(this, routineId)
    }

    fun loadViews(){
        tvRoutineTitle = findViewById(R.id.tvRoutineTitle)
        edRoutineName = findViewById(R.id.etRoutineName)
        buttonContainer = findViewById(R.id.containerButtonAction)
        btnNewAction = findViewById(R.id.btnNewAction)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnTime= findViewById(R.id.btnTime)
        btnSaveRoutine = findViewById(R.id.btnSaveRoutine)
        btnDelete = findViewById(R.id.btnDelete)
    }

    fun loadObservers(){
        viewModel.routine.observe(this) { (routine, actions) ->
            tvRoutineTitle.text = "${routine.name} Routine"

            btnTime.setOnClickListener {
                showTimePicker(routine)
            }

            if (edRoutineName.text.toString() != routine.name) {
                edRoutineName.setText(routine.name)
                edRoutineName.setSelection(routine.name.length)
            }

            btnTime.text = String.format("%02d:%02d",
                routine.triggerTime.get(Calendar.HOUR_OF_DAY),
                routine.triggerTime.get(Calendar.MINUTE)
            )

            buttonContainer.layoutManager = LinearLayoutManager(this)
            buttonContainer.adapter = ActionAdapter(actions, this)
        }
    }

    fun loadActions(){
        edRoutineName.doAfterTextChanged {
            viewModel.setRoutineName(this, it.toString())
        }

        btnSaveRoutine.setOnClickListener {
            viewModel.saveRoutine(this, edRoutineName.text.toString())
            finish()
        }

        btnNewAction.setOnClickListener {
            val intent = Intent(this, CreateActionActivity::class.java)
            intent.putExtra("routineId", routineId)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            viewModel.deleteRoutine(this)
            finish()
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("routineId", routineId)
            startActivity(intent)
        }
    }

    fun showTimePicker(routine: RoutineEntity){
        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val triggerTime = Calendar.getInstance()
                triggerTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                triggerTime.set(Calendar.MINUTE, selectedMinute)
                viewModel.setTriggerTime(this, triggerTime)
            },
            routine.triggerTime.get(Calendar.HOUR_OF_DAY),
            routine.triggerTime.get(Calendar.MINUTE),
            true
        ).show()
    }
}