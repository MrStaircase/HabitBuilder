package com.example.habitbuilder.notification.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.habitbuilder.notification.worker.ActionWorker
import com.example.habitbuilder.data.entity.CompletionEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.CompletionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val routineId = intent.getIntExtra("routineId", -1)
        val actionIndex = intent.getIntExtra("actionIndex", 0)
        val notificationId = intent.getIntExtra("notificationId", -1)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

        CoroutineScope(Dispatchers.IO).launch {
            val actions = ActionRepository.getAll(context, routineId)
            CompletionRepository.insert(context,
                CompletionEntity(
                    routineId = routineId,
                    actionId = actions[actionIndex].id,
                    date = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0) }
                )
            )
            val nextIndex = actionIndex + 1
            if (nextIndex < actions.size) {
                val delay = 0.toLong()

                val data = workDataOf(
                    "routineId" to routineId,
                    "actionIndex" to nextIndex
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