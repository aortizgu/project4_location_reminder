# Location Reminder

A Todo list app with location reminders that remind the user to do something when he reaches a specific location. The app will require the user to create an account and login to set and access reminders.

## Getting Started

1. Clone the project to your local machine.
2. Open the project using Android Studio.

### Dependencies

```
1. A created project on Firebase console.
2. A create a project on Google console.
```

### Installation

Step by step explanation of how to get a dev environment running.

```
1. To enable Firebase Authentication:
        a. Go to the authentication tab at the Firebase console and enable Email/Password and Google Sign-in methods.
        b. download `google-services.json` and add it to the app.
2. To enable Google Maps:
    a. Go to APIs & Services at the Google console.
    b. Select your project and go to APIs & Credentials.
    c. Create a new api key and restrict it for android apps.
    d. Add your package name and SHA-1 signing-certificate fingerprint.
    c. Enable Maps SDK for Android from API restrictions and Save.
    d. Copy the api key to the `google_maps_api.xml`
3. Run the app on your mobile phone or emulator with Google Play Services in it.
```

## Testing

Right click on the `test` or `androidTest` packages and select Run Tests

### Break Down Tests

Explain what each test does and why

```
1.androidTest
 * RemindersActivityTest:
  - createReminder_noTitle: check that cannot save a reminder if no title setted.
  - createReminder_noLocation: check that cannot save a reminder if no location setted.
 * RemindersDaoTest:
  - insertReminderAndGetById: inserts a reminder and get it by id
  - insertReminderAndDelete: inserts a reminder , delete all and check that no reminders
  - insertReminderAndGetByIdError: try to get an invalid reminder and check that returns null
 * RemindersLocalRepositoryTest:
  - saveReminder_retrievesReminder: inserts a reminder and get it by id
 * ReminderListFragmentTest:
  - clickAddReminder_navigateToSaveFragment: check that when click on AddReminder button, app navigates to SaveFragment
  - noDataTextView_isShown: check that if reminder list is empty, a no data drawn is shown
  - noDataTextView_isNotShown: check that if reminder list is not empty, a no data drawn is not shown
  - reminderlist_areShown: check that a list of reminders is shown in the ReminderListFragment
  - forceDataSourceError_isShown: check that if reminder list cannot be loaded, an error is shown in a snack bar
2. test
 * RemindersListViewModelTest:
  - loadReminders_emptyList: check that remindersListViewModel show emptyList flag
  - loadReminders_noEmptyList: check that remindersListViewModel doesn't show emptyList flag
  - loadReminders_showLoading: check that remindersListViewModel shows loading
  - loadReminders_forceError: check that remindersListViewModel shows an error when loading it from datasource
 * SaveReminderViewModelTest:
  - validateAndSaveReminder_ok: 
        - check that saveReminderViewModel shows a toast when a reminder is saved
        - check that saveReminderViewModel navigates back when a reminder is saved
        - check that saveReminderViewModel shows a loading state when a reminder is being saved
  - validateEnteredData_possibleCasses: check that saveReminderViewModel validates a reminder when tries to save it
```

## Project Instructions
    1. Create a Login screen to ask users to login using an email address or a Google account.  Upon successful login, navigate the user to the Reminders screen.   If there is no account, the app should navigate to a Register screen.
    2. Create a Register screen to allow a user to register using an email address or a Google account.
    3. Create a screen that displays the reminders retrieved from local storage. If there are no reminders, display a   "No Data"  indicator.  If there are any errors, display an error message.
    4. Create a screen that shows a map with the user's current location and asks the user to select a point of interest to create a reminder.
    5. Create a screen to add a reminder when a user reaches the selected location.  Each reminder should include
        a. title
        b. description
        c. selected location
    6. Reminder data should be saved to local storage.
    7. For each reminder, create a geofencing request in the background that fires up a notification when the user enters the geofencing area.
    8. Provide testing for the ViewModels, Coroutines and LiveData objects.
    9. Create a FakeDataSource to replace the Data Layer and test the app in isolation.
    10. Use Espresso and Mockito to test each screen of the app:
        a. Test DAO (Data Access Object) and Repository classes.
        b. Add testing for the error messages.
        c. Add End-To-End testing for the Fragments navigation.


## Student Deliverables:

1. APK file of the final project. Found inside apk folder
2. Git Repository with the code. 

## Built With

* [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection framework for Kotlin.
* [FirebaseUI Authentication](https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md) - FirebaseUI provides a drop-in auth solution that handles the UI flows for signing
* [JobIntentService](https://developer.android.com/reference/androidx/core/app/JobIntentService) - Run background service from the background application, Compatible with >= Android O.

## License
