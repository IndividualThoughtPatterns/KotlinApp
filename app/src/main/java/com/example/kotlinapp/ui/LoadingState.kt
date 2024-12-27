package com.example.kotlinapp.ui

data class LoadingState(
    val isLoaded: Boolean,
    val error: Throwable?
)
