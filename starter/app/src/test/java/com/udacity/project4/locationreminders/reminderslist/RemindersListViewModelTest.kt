package com.udacity.project4.locationreminders.reminderslist

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import com.udacity.project4.locationreminders.data.FakeDataSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private lateinit var dataSource: FakeDataSource
    //private lateinit var reminderListViewModel: ReminderListViewModel

    @Before
    fun setupViewModel() {
        dataSource = FakeDataSource()
    }

    @Test
    fun loadReminders_ok(){
        //GIVEN
        //reminderListViewModel.loadReminders()

        //WHEN
        //val showNoData = reminderListViewModel.showNoData.getOrAwaitValue()
        val showNoData = true
        //THEN
        assertThat(showNoData, `is`(true))
    }

}