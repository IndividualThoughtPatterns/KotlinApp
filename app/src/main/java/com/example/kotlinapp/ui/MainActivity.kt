package com.example.kotlinapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kotlinapp.ui.pokemonlist.PokemonListViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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