package com.udacity.project4.locationreminders.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT
import timber.log.Timber

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive action? $intent.action")
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                when (geofencingEvent.errorCode) {
                    GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> Timber.e("onReceive: error, GEOFENCE_NOT_AVAILABLE")
                    GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> Timber.e("onReceive: error, GEOFENCE_TOO_MANY_GEOFENCES")
                    GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> Timber.e("onReceive: error, GEOFENCE_TOO_MANY_PENDING_INTENTS")
                    else -> Timber.e("onReceive: error, GEOFENCE_ERROR")
                }
            } else if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER && geofencingEvent.triggeringGeofences.isNotEmpty()) {
                GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
            }
        }
    }

}