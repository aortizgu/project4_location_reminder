package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeAndroidTestDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    private var isError = false

    fun setError(value: Boolean) {
        isError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (isError) {
            return Result.Error(("error message"))
        }
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error("No list")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.firstOrNull { it.id == id }?.let {
            return Result.Success(it)
        }

        return Result.Error("No reminder")
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

}