package com.example.prm1.viewmodel

import androidx.lifecycle.ViewModel

class LoginModel : ViewModel() {

    private val correctPassword = "1234"

    fun isPasswordCorrect(password: String): Boolean {
        return password == correctPassword
    }
}

