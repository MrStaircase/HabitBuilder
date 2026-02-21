package com.example.habitbuilder.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.habitbuilder.R
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.viewmodel.RoutineViewModel
import java.util.Calendar

class CreateActionActivity : ComponentActivity() {
    val viewModel: RoutineViewModel by viewModels()
    private var routineId: Int = -1
    private lateinit var edActionDescription: EditText
    private lateinit var edDuration: EditText
    private lateinit var btnSaveAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_action)

        routineId = intent.getIntExtra("routineId", -1)

        loadViews()
        loadObservers()
        loadActions()
    }

    fun loadViews(){
        edActionDescription = findViewById(R.id.edActionDescription)
        edDuration = findViewById(R.id.edDuration)
        btnSaveAction = findViewById(R.id.btnSaveAction)
    }

    fun loadObservers(){
        viewModel.actionId.observe(this){
            finish()
        }

        viewModel.actionDescription.observe(this){ value ->
            if (edActionDescription.text.toString() != value) {
                edActionDescription.setText(value)
                edActionDescription.setSelection(value.length)
            }
        }

        viewModel.actionDuration.observe(this){ value ->
            val display = value?.toString() ?: ""
            if (edDuration.text.toString() != display) {
                edDuration.setText(display)
                edDuration.setSelection(display.length)
            }
        }
    }

    fun loadActions(){
        edActionDescription.doAfterTextChanged {
            viewModel.setActionDescription(it.toString())
        }

        edDuration.doAfterTextChanged {
            viewModel.setActionDuration(it.toString().toIntOrNull())
        }

        btnSaveAction.setOnClickListener {
            val description = edActionDescription.text.toString()
            if(description.isEmpty()){
                showEmptyNameAlert()
            }
            else{
                val creationDate = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val duration = edDuration.text.toString().toIntOrNull() ?: 0
                val action = ActionEntity(
                    routineId = routineId,
                    description = description,
                    creationDate = creationDate,
                    durationMinutes = duration
                )

                viewModel.createAction(this, action)
            }
        }
    }

    fun showEmptyNameAlert(){
        AlertDialog.Builder(this)
            .setTitle("Invalid Input")
            .setMessage("The description cannot be empty.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}