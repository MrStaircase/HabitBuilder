package com.example.habitbuilder.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.habitbuilder.notification.worker.ActionWorker
import com.example.habitbuilder.notification.receiver.CompletedReceiver
import com.example.habitbuilder.notification.receiver.InProgressReceiver
import com.example.habitbuilder.R
import com.example.habitbuilder.data.Routine
import com.example.habitbuilder.notification.receiver.SkipReceiver
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationHelper {
    const val CHANNEL_ID = "habit_builder_channel"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Habit Builder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun scheduleFirstAction(context: Context, routine: Routine) {
        val now = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val triggerTime = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            set(Calendar.HOUR_OF_DAY, routine.triggerTime.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, routine.triggerTime.get(Calendar.MINUTE))
        }

        val delay = triggerTime.timeInMillis - now.timeInMillis

        val data = workDataOf(
            "routineId" to routine.id,
            "actionIndex" to 0
        )

        val request = OneTimeWorkRequestBuilder<ActionWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    fun showNotification(
        context: Context,
        routineId: Int,
        actionIndex: Int,
        title: String,
        text: String
    ) {
        val notificationId = routineId * 1000 + actionIndex

        fun <T> actionImpendingIntent(cls: Class<T>, requestCode: Int): PendingIntent{
            val completedIntent = Intent(context, cls).apply {
                putExtra("routineId", routineId)
                putExtra("actionIndex", actionIndex)
                putExtra("notificationId", notificationId)
            }

            val completedPending = PendingIntent.getBroadcast(
                context,
                requestCode,
                completedIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            return completedPending
        }

        val completedPending = actionImpendingIntent(
            CompletedReceiver::class.java,
            routineId * 10000 + actionIndex * 100
        )

        val inProgressPending = actionImpendingIntent(
            InProgressReceiver::class.java,
            routineId * 10000 + actionIndex * 100 + 1
        )

        val skipPending = actionImpendingIntent(
            SkipReceiver::class.java,
            routineId * 10000 + actionIndex * 100 + 2
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .addAction(R.drawable.ic_check, "Completed", completedPending)
            .addAction(R.drawable.ic_check, "In Progress", inProgressPending)
            .addAction(R.drawable.ic_check, "Skip", skipPending)
            .setAutoCancel(false)
            .build()

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(notificationId, notification)
    }
}