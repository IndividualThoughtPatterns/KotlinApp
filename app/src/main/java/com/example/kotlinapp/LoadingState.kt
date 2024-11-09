package com.example.kotlinapp

data class LoadingState(
    val isLoaded: Boolean,
    val error: Throwable?
)
