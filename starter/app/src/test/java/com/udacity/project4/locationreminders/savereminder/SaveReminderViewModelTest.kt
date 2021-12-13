package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var dataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    @Before
    fun setupViewModel() {
        stopKoin()
        dataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun validateAndSaveReminder_ok()  = runBlocking {
        mainCoroutineRule.pauseDispatcher()
        val remonderAdded = ReminderDataItem("title", "description", "location", 0.0, 0.0, "id")

        saveReminderViewModel.validateAndSaveReminder(remonderAdded)
        MatcherAssert.assertThat(
            saveReminderViewModel.showLoading.getOrAwaitValue(),
            Is.`is`(true)
        )
        mainCoroutineRule.resumeDispatcher()

        val result = dataSource.getReminder("id")
        MatcherAssert.assertThat(
            result is Result.Success<ReminderDTO>,
            Is.`is`(true)
        )
        if (result is Result.Success<ReminderDTO>) {
            val reminderDTO = result.data
            MatcherAssert.assertThat(
                reminderDTO?.title,
                Is.`is`(remonderAdded.title)
            )
            MatcherAssert.assertThat(
                reminderDTO?.description,
                Is.`is`(remonderAdded.description)
            )
            MatcherAssert.assertThat(
                reminderDTO?.location,
                Is.`is`(remonderAdded.location)
            )
            MatcherAssert.assertThat(
                reminderDTO?.latitude,
                Is.`is`(remonderAdded.latitude)
            )
            MatcherAssert.assertThat(
                reminderDTO?.longitude,
                Is.`is`(remonderAdded.longitude)
            )
            MatcherAssert.assertThat(
                reminderDTO?.id,
                Is.`is`(remonderAdded.id)
            )
        }
        MatcherAssert.assertThat(
            saveReminderViewModel.showToast.getOrAwaitValue(),
            Is.`is`("Reminder Saved !")
        )
        MatcherAssert.assertThat(
            saveReminderViewModel.navigationCommand.getOrAwaitValue(),
            Is.`is`(NavigationCommand.Back)
        )
    }

    @Test
    fun validateEnteredData_possibleCasses() {
        MatcherAssert.assertThat(
            saveReminderViewModel.validateEnteredData(
                ReminderDataItem(
                    "title",
                    "description",
                    "location",
                    0.0,
                    0.0
                )
            ), Is.`is`(true)
        )
        MatcherAssert.assertThat(
            saveReminderViewModel.validateEnteredData(
                ReminderDataItem(
                    null,
                    "description",
                    "location",
                    0.0,
                    0.0
                )
            ), Is.`is`(false)
        )
        MatcherAssert.assertThat(
            saveReminderViewModel.validateEnteredData(
                ReminderDataItem(
                    "title",
                    "description",
                    null,
                    0.0,
                    0.0
                )
            ), Is.`is`(false)
        )
    }
}