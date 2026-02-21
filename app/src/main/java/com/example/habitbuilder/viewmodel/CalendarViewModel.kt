package com.example.habitbuilder.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitbuilder.CalendarItem
import com.example.habitbuilder.Status
import com.example.habitbuilder.data.DailyCompletion
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.RoutineEntity
import com.example.habitbuilder.data.repository.ActionRepository
import com.example.habitbuilder.data.repository.CompletionRepository
import com.example.habitbuilder.data.repository.RoutineRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarViewModel() : ViewModel() {

    lateinit var context: Context
    var routineId: Int = -1
    val routineTitle = MutableLiveData<String>()
    val monthItems = MutableLiveData<List<CalendarItem>>()
    val selectedDayMessage = MutableLiveData<Pair<String, String>>()

    val currentMonthTitle = MutableLiveData<String>()

    private var currentMonthStart: Calendar =
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    private fun loadMonthTitle(){
        val monthName = currentMonthStart.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )

        val year = currentMonthStart.get(Calendar.YEAR)

        currentMonthTitle.postValue("$monthName $year")
    }

    fun nextMonth() {
        currentMonthStart.add(Calendar.MONTH, 1)
        loadMonth()
    }

    fun previousMonth() {
        currentMonthStart.add(Calendar.MONTH, -1)
        loadMonth()
    }

    fun loadRoutine() {
        viewModelScope.launch {
            val routine = RoutineRepository.get(context, routineId)
            routine?.let {
                routineTitle.postValue("${it.name} Calendar")
            }
        }
    }

    fun loadMonth() {
        loadMonthTitle()

        viewModelScope.launch {
            val actions = ActionRepository.getAll(context, routineId)
            val completedByDay = CompletionRepository.getCountPerDay(context, routineId)
            val routine = RoutineRepository.get(context, routineId)

            routine?.let {
                val items = buildCalendarItems(actions, completedByDay, routine)
                monthItems.postValue(items)
            }
        }
    }

    private fun buildCalendarItems(
        actions: List<ActionEntity>,
        completedByDay: List<DailyCompletion>,
        routine: RoutineEntity
    ): List<CalendarItem> {

        val items = mutableListOf<CalendarItem>()
        val firstDay = currentMonthStart.clone() as Calendar
        val today = Calendar.getInstance()

        addLeadingEmptyDays(items, firstDay)

        val daysInMonth = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 0 until daysInMonth) {
            val date = (firstDay.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, i)
            }
            val status = calculateStatus(date, today, routine, actions, completedByDay)
            items.add(CalendarItem.Day(date, status))
        }

        return items
    }

    private fun addLeadingEmptyDays(
        items: MutableList<CalendarItem>,
        firstDay: Calendar
    ) {
        val leadingEmptyCount = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % 7
        repeat(leadingEmptyCount) {
            items.add(CalendarItem.Empty)
        }
    }

    private fun calculateStatus(
        date: Calendar,
        today: Calendar,
        routine: RoutineEntity,
        actions: List<ActionEntity>,
        completedByDay: List<DailyCompletion>
    ): Status {

        val actionsInDay =
            actions.filter { it.creationDate.timeInMillis <= date.timeInMillis }

        val matchingCompletion = completedByDay.firstOrNull {
            sameDay(it.date, date)
        }

        val completedCount = matchingCompletion?.completedActions ?: 0

        return when {
            date.before(routine.creationDate) -> Status.NONE
            date.after(today) && completedCount == 0 -> Status.NONE
            completedCount == 0 -> Status.RED
            completedCount == actionsInDay.size -> Status.GREEN
            else -> Status.YELLOW
        }
    }

    fun onDayClicked(date: Calendar) {
        viewModelScope.launch {
            val allActions = ActionRepository.getAll(context, routineId)
            val actionsInDate =
                allActions.filter { it.creationDate.timeInMillis <= date.timeInMillis }

            val tempDate = date.clone() as Calendar
            tempDate.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val completions =
                CompletionRepository.get(context, routineId, tempDate)

            val completedIds = completions.map { it.actionId }.toSet()

            val message = actionsInDate.joinToString("\n") { action ->
                val done = if (completedIds.contains(action.id)) "✔" else "✘"
                "$done ${action.description}"
            }

            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)

            selectedDayMessage.postValue("Actions on $formattedDate" to message)
        }
    }

    private fun sameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}
