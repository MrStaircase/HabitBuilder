package com.example.habitbuilder.notification.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import com.example.habitbuilder.notification.NotificationHelper

class ActionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val routineId = inputData.getInt("routineId", -1)
        val actionIndex = inputData.getInt("actionIndex", 0)

        val routine = RoutineRepository.get(applicationContext, routineId)
        val actions = ActionRepository.getAll(applicationContext, routineId)
        val action = actions[actionIndex]

        routine?.let {
            NotificationHelper.showNotification(
                applicationContext,
                routineId,
                actionIndex,
                action.description,
                routine.name
            )

            return Result.success()
        }
        return Result.failure()
    }
}