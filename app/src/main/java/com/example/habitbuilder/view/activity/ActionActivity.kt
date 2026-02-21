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

class ActionActivity : ComponentActivity() {
    private val viewModel: ActionViewModel by viewModels()
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
        viewModel.loadAction(this, actionId)
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
            edActionDescription.setText(action.description)
            edDurationMinutes.setText(action.durationMinutes.toString())
        }


        viewModel.actionDescription.observe(this){ value ->
            if (edActionDescription.text.toString() != value) {
                edActionDescription.setText(value)
                edActionDescription.setSelection(value.length)
            }
        }

        viewModel.actionDuration.observe(this){ value ->
            val display = value?.toString() ?: ""
            if (edDurationMinutes.text.toString() != display) {
                edDurationMinutes.setText(display)
                edDurationMinutes.setSelection(display.length)
            }
        }
    }

    fun loadActions(){
        edActionDescription.doAfterTextChanged {
            viewModel.setActionDescription(it.toString())
        }

        edDurationMinutes.doAfterTextChanged {
            viewModel.setActionDuration(it.toString().toIntOrNull())
        }

        btnSaveAction.setOnClickListener {
            viewModel.saveAction(
                this,
                edActionDescription.text.toString(),
                edDurationMinutes.text.toString().toIntOrNull() ?: 0
            )
            finish()
        }

        btnDeleteAction.setOnClickListener {
            viewModel.deleteAction(this)
            finish()
        }
    }
}