package com.example.habitbuilder.view.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.habitbuilder.R
import com.example.habitbuilder.viewmodel.ActionViewModel
import com.example.habitbuilder.viewmodel.TempViewModel

class ActionActivity : ComponentActivity() {
    private val viewModel: ActionViewModel by viewModels{ ActionViewModel.Factory }
    private val tempViewModel: TempViewModel by viewModels()
    private var actionId: Int = -1
    private lateinit var tvActionTitle: TextView
    private lateinit var edActionDescription: EditText
    private lateinit var edDurationMinutes: EditText
    private lateinit var btnSaveAction: Button
    private lateinit var btnDeleteAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action)

        actionId = intent.getIntExtra("actionId", -1)

        loadViews()
        loadObserver()
        loadActions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAction(actionId)
    }

    fun loadViews(){
        tvActionTitle = findViewById(R.id.actionTitle)
        edActionDescription = findViewById(R.id.etActionDescription)
        edDurationMinutes = findViewById(R.id.etDurationMinutes)
        btnSaveAction = findViewById(R.id.btnSaveAction)
        btnDeleteAction = findViewById(R.id.btnDeleteAction)
    }

    fun loadObserver(){
        viewModel.action.observe(this) { action ->
            tvActionTitle.text = action.description

            if (tempViewModel.description.value == null){
                edActionDescription.setText(action.description)
            }
            if (tempViewModel.duration.value == null) {
                edDurationMinutes.setText(action.durationMinutes.toString())
            }
        }

        tempViewModel.description.observe(this){ description ->
            if (edActionDescription.text.toString() != description) {
                edActionDescription.setText(description)
                edActionDescription.setSelection(description.length)
            }
        }

        tempViewModel.duration.observe(this){ durationMinutes ->
            val display = durationMinutes.toString()
            if (edDurationMinutes.text.toString() != display) {
                edDurationMinutes.setText(display)
                edDurationMinutes.setSelection(display.length)
            }
        }
    }

    fun loadActions(){
        edActionDescription.doAfterTextChanged {
            tempViewModel.setActionDescription(it.toString())
        }

        edDurationMinutes.doAfterTextChanged {
            tempViewModel.setActionDuration(it.toString().toIntOrNull())
        }

        btnSaveAction.setOnClickListener {
            viewModel.saveAction(
                edActionDescription.text.toString(),
                edDurationMinutes.text.toString().toIntOrNull() ?: 0
            )
            finish()
        }

        btnDeleteAction.setOnClickListener {
            viewModel.deleteAction()
            finish()
        }
    }
}