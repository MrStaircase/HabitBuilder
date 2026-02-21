package com.example.habitbuilder.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.view.adapter.CalendarAdapter
import com.example.habitbuilder.view.OnDayClickListener
import com.example.habitbuilder.R
import com.example.habitbuilder.viewmodel.CalendarViewModel
import java.util.Calendar

class CalendarActivity : ComponentActivity(), OnDayClickListener {
    private val viewModel: CalendarViewModel by viewModels()
    private var routineId: Int = -1
    private lateinit var tvRoutineTitle: TextView
    private lateinit var calendarRecycler: RecyclerView
    private lateinit var tvMonthLabel: TextView
    private lateinit var btnPreviousMonth: Button
    private lateinit var btnNextMonth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        routineId = intent.getIntExtra("routineId", -1)

        loadViews()
        loadObserves()
        loadActions()

        viewModel.loadRoutine(this, routineId)
        viewModel.loadMonth(this, routineId)
    }

    fun loadViews(){
        tvRoutineTitle = findViewById(R.id.tvRoutineTitle)
        calendarRecycler = findViewById(R.id.calendarRecycler)
        tvMonthLabel = findViewById(R.id.tvMonthLabel)
        btnPreviousMonth = findViewById(R.id.btnPreviousMonth)
        btnNextMonth = findViewById(R.id.btnNextMonth)
    }

    fun loadObserves(){
        viewModel.routineTitle.observe(this){ routineTitle ->
            tvRoutineTitle.text = routineTitle
        }

        viewModel.monthItems.observe(this){
            calendarRecycler.layoutManager = GridLayoutManager(this, 7)
            calendarRecycler.adapter = CalendarAdapter(it, this)
        }

        viewModel.selectedDayMessage.observe(this) { (title, message) ->
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        viewModel.currentMonthTitle.observe(this){
            tvMonthLabel.text = it
        }
    }

    fun loadActions(){
        btnPreviousMonth.setOnClickListener {
            viewModel.previousMonth(this, routineId)
        }

        btnNextMonth.setOnClickListener {
            viewModel.nextMonth(this, routineId)
        }
    }

    override fun onDayClick(date: Calendar) {
        viewModel.onDayClicked(this, routineId, date)
    }
}