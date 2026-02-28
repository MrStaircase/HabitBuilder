package com.example.habitbuilder.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.R
import com.example.habitbuilder.notification.NotificationHelper
import com.example.habitbuilder.view.adapter.RoutineAdapter
import com.example.habitbuilder.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels{ MainViewModel.Factory }
    private lateinit var buttonContainer: RecyclerView
    private lateinit var btnNewRoutine: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadViews()
        loadObservers()
        loadActions()

        checkPermissions()
        NotificationHelper.createChannel(this)

        // for debug purposes
//        viewModel.createTestData()
    }

    fun loadViews(){
        buttonContainer = findViewById(R.id.buttonContainer)
        btnNewRoutine = findViewById(R.id.btnNewRoutine)
    }

    fun loadObservers(){
        viewModel.routines.observe(this) { routines ->
            buttonContainer.layoutManager = LinearLayoutManager(this)
            buttonContainer.adapter = RoutineAdapter(routines, this)
        }
    }

    fun loadActions(){
        btnNewRoutine.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivity(intent)
        }
    }

    fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRoutines()
    }
}