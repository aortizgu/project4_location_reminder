package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.koin.core.context.stopKoin


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var dataSource: FakeDataSource
    private lateinit var remindersListViewModel: RemindersListViewModel

    @Before
    fun setupViewModel() {
        stopKoin()
        dataSource =
            FakeDataSource(mutableListOf(ReminderDTO("title", "description", "location", 0.0, 0.0)))
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun loadReminders_emptyList() = runBlocking {
        dataSource.deleteAllReminders()
        remindersListViewModel.loadReminders()

        val showNoData = remindersListViewModel.showNoData.getOrAwaitValue()

        assertThat(showNoData, `is`(true))
    }

    @Test
    fun loadReminders_noEmptyList() {
        remindersListViewModel.loadReminders()

        val showNoData = remindersListViewModel.showNoData.getOrAwaitValue()

        assertThat(showNoData, `is`(false))
    }

    @Test
    fun loadReminders_showLoading() {
        mainCoroutineRule.pauseDispatcher()

        remindersListViewModel.loadReminders()

        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadReminders_forceError() {
        dataSource.setError(true)
        remindersListViewModel.loadReminders()

        val showNoData = remindersListViewModel.showNoData.getOrAwaitValue()
        val errorMessage = remindersListViewModel.showSnackBar.getOrAwaitValue()

        assertThat(showNoData, `is`(true))
        assertThat(errorMessage, `is`("error message"))
    }
}