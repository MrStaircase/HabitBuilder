package com.example.habitbuilder.notification.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.habitbuilder.notification.worker.ActionWorker
import com.example.habitbuilder.data.repository.ActionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class InProgressReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val routineId = intent.getIntExtra("routineId", -1)
        val actionIndex = intent.getIntExtra("actionIndex", 0)
        val notificationId = intent.getIntExtra("notificationId", -1)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)


        CoroutineScope(Dispatchers.IO).launch {
            val actions = ActionRepository.getAll(context, routineId)
            if (actionIndex < actions.size) {
                val delay = actions[actionIndex].durationMinutes.toLong()

                val data = workDataOf(
                    "routineId" to routineId,
                    "actionIndex" to actionIndex
                )

                val request = OneTimeWorkRequestBuilder<ActionWorker>()
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .setInputData(data)
                    .build()

                WorkManager.Companion.getInstance(context).enqueue(request)
            }
        }
    }
}