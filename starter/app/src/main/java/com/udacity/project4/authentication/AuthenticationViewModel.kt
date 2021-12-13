package com.udacity.project4.authentication

import android.app.Application
import androidx.lifecycle.map
import androidx.lifecycle.AndroidViewModel

class AuthenticationViewModel(app: Application) : AndroidViewModel(app) {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}