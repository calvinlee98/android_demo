package com.example.android_demo_application.fragments

interface LoginView {
    fun onSuccess()
    fun onFail(s: String?)
}