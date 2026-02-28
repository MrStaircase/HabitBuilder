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
import com.example.habitbuilder.viewmodel.RoutineViewModel
import com.example.habitbuilder.viewmodel.TempViewModel
import java.util.Calendar

class RoutineActivity : ComponentActivity() {
    private val viewModel: RoutineViewModel by viewModels{ RoutineViewModel.Factory }
    private val tempViewModel: TempViewModel by viewModels()
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
        viewModel.loadRoutine(routineId)
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
        viewModel.routine.observe(this) { routine ->
            tvRoutineTitle.setText("${routine.name} Routine")


            if (tempViewModel.name.value == null){
                edRoutineName.setText(routine.name)
            }

            if (tempViewModel.triggerTime.value == null){
                btnTime.setOnClickListener {
                    showTimePicker(routine.triggerTime)
                }

                btnTime.text = String.format("%02d:%02d",
                    routine.triggerTime.get(Calendar.HOUR_OF_DAY),
                    routine.triggerTime.get(Calendar.MINUTE)
                )
            }

            buttonContainer.layoutManager = LinearLayoutManager(this)
            buttonContainer.adapter = ActionAdapter(routine.actions, this)
        }

        tempViewModel.name.observe(this){ name ->
            if (edRoutineName.text.toString() != name) {
                edRoutineName.setText(name)
                edRoutineName.setSelection(name.length)
            }
        }

        tempViewModel.triggerTime.observe(this){ triggerTime ->
            btnTime.setOnClickListener {
                showTimePicker(triggerTime)
            }

            btnTime.text = String.format("%02d:%02d",
                triggerTime.get(Calendar.HOUR_OF_DAY),
                triggerTime.get(Calendar.MINUTE)
            )
        }
    }

    fun loadActions(){
        edRoutineName.doAfterTextChanged {
            tempViewModel.setRoutineName(it.toString())
        }

        btnSaveRoutine.setOnClickListener {
            var time: Calendar = Calendar.getInstance()
            tempViewModel.triggerTime.value?.let {
                time = it
            } ?: run {
                viewModel.routine.value?.let {
                    time = it.triggerTime
                }
            }
            viewModel.saveRoutine(edRoutineName.text.toString(), time)
            finish()
        }

        btnNewAction.setOnClickListener {
            val intent = Intent(this, CreateActionActivity::class.java)
            intent.putExtra("routineId", routineId)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            viewModel.deleteRoutine()
            finish()
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("routineId", routineId)
            startActivity(intent)
        }
    }

    fun showTimePicker(triggerTime: Calendar){
        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val triggerTime = Calendar.getInstance()
                triggerTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                triggerTime.set(Calendar.MINUTE, selectedMinute)
                tempViewModel.setTriggerTime(triggerTime)
            },
            triggerTime.get(Calendar.HOUR_OF_DAY),
            triggerTime.get(Calendar.MINUTE),
            true
        ).show()
    }
}