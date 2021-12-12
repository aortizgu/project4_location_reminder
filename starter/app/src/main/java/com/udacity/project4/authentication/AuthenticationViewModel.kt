package com.udacity.project4.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.project4.utils.FirebaseAuthStateLiveData

class AuthenticationViewModel(app: Application) : AndroidViewModel(app) {
    val authenticationState = FirebaseAuthStateLiveData()
}