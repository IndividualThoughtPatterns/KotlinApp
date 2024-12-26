package com.example.kotlinapp.data

data class LoadingState(
    val isLoaded: Boolean,
    val error: Throwable?
)
