package com.example.kotlinapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Main()
        }
    }

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.CREATED) {
//                pokemonListViewModel.nextPageLoadingStateFlow.collect {
//                    it?.let {
//                        if (it.isLoaded) {
//                            showSuccessMessage()
//                        } else {
//                            handleNetworkError()
//                            Log.d(
//                                "next page loading failure",
//                                it.error!!.message.toString()
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun handleNetworkError() {
//        Toast.makeText(this, "Ошибка сети", Toast.LENGTH_LONG).show()
//    }
//
//    private fun showSuccessMessage() {
//        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_LONG).show()
//    }
}