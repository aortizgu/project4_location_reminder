package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeAndroidTestDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest(){

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setup() {
        stopKoin()
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { FakeAndroidTestDataSource() as ReminderDataSource }
        }
        startKoin {
            modules(listOf(myModule))
        }
        repository = get()

        runBlocking {
            repository.deleteAllReminders()
        }
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

     @Test
    fun clickAddReminder_navigateToSaveFragment() {
        val scenario: FragmentScenario<ReminderListFragment> = launchFragmentInContainer(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(fragmentScenario = scenario)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.addReminderFAB))
            .perform(click())

        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

    @Test
    fun noDataTextView_isShown() {
        val scenario: FragmentScenario<ReminderListFragment> = launchFragmentInContainer(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(fragmentScenario = scenario)

        onView(withId(R.id.noDataTextView)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun noDataTextView_isNotShown() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 0.0, 0.0)
        repository.saveReminder(reminder)

        val scenario: FragmentScenario<ReminderListFragment> = launchFragmentInContainer(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(fragmentScenario = scenario)

        onView(withId(R.id.noDataTextView)).check(ViewAssertions.matches(not(isDisplayed())))
    }

    @Test
    fun reminderlist_areShown() = runBlockingTest {
        repository.saveReminder(ReminderDTO("title1", "description", "location", 0.0, 0.0))
        repository.saveReminder(ReminderDTO("title2", "description", "location", 0.0, 0.0))
        repository.saveReminder(ReminderDTO("title3", "description", "location", 0.0, 0.0))
        val scenario: FragmentScenario<ReminderListFragment> = launchFragmentInContainer(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(fragmentScenario = scenario)

        onView(withText("title1")).check(matches(isDisplayed()))
        onView(withText("title2")).check(matches(isDisplayed()))
        onView(withText("title3")).check(matches(isDisplayed()))
    }

    @Test
    fun forceDataSourceError_isShown(){
        (repository as FakeAndroidTestDataSource).setError(true)
        val scenario: FragmentScenario<ReminderListFragment> = launchFragmentInContainer(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(fragmentScenario = scenario)
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("error message")))
    }
}