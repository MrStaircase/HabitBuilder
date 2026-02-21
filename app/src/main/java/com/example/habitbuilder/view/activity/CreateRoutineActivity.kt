package com.example.habitbuilder.view.activity

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.habitbuilder.R
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.viewmodel.MainViewModel
import java.util.Calendar

class CreateRoutineActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
    private var triggerTime = Calendar.getInstance()
    private lateinit var edRoutineName: EditText
    private lateinit var btnTriggerTime: Button
    private lateinit var btnSaveRoutine: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        loadViews()
        loadObservers()
        loadActions()
    }

    fun loadViews(){
        edRoutineName = findViewById(R.id.edRoutineName)
        btnTriggerTime = findViewById(R.id.btnTriggerTime)
        btnSaveRoutine = findViewById(R.id.btnSaveRoutine)
    }

    fun loadObservers(){
        viewModel.routineId.observe(this){
            val intent = Intent(this, RoutineActivity::class.java)
            intent.putExtra("routineId", it)
            startActivity(intent)
            finish()
        }

        viewModel.routineName.observe(this){ value ->
            if (edRoutineName.text.toString() != value) {
                edRoutineName.setText(value)
                edRoutineName.setSelection(value.length)
            }
        }

        viewModel.triggerTime.observe(this){
            triggerTime = it
            btnTriggerTime.text = String.format("%02d:%02d",
                it.get(Calendar.HOUR_OF_DAY),
                it.get(Calendar.MINUTE)
            )
        }
    }

    fun loadActions(){
        edRoutineName.doAfterTextChanged {
            viewModel.setRoutineName(it.toString())
        }

        btnTriggerTime.setOnClickListener {
            showTimePicker()
        }

        btnSaveRoutine.setOnClickListener {
            val name = edRoutineName.text.toString()
            if(name.isEmpty()){
                showEmptyNameAlert()
            }
            else{
                val creationDate = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val triggerTime: Calendar = triggerTime
                val routine = RoutineEntity(
                    name = name,
                    creationDate = creationDate,
                    triggerTime = triggerTime
                )

                viewModel.createRoutine(this, routine)
            }
        }
    }

    fun showEmptyNameAlert(){
        AlertDialog.Builder(this)
            .setTitle("Invalid Input")
            .setMessage("The name cannot be empty.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showTimePicker(){
        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val triggerTime = Calendar.getInstance()
                triggerTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                triggerTime.set(Calendar.MINUTE, selectedMinute)
                viewModel.setTriggerTime(triggerTime)
            },
            triggerTime.get(Calendar.HOUR_OF_DAY),
            triggerTime.get(Calendar.MINUTE),
            true
        ).show()
    }
}