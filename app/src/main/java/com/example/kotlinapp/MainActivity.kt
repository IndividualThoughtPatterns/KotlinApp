package com.example.kotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import android.util.Log
import android.net.Uri
import org.json.JSONObject
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.response)

        val client = OkHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon?limit=1"
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            val json = JSONObject(response.body!!.string())
                            val results = json.getJSONArray("results")
                            val name = results.getJSONObject(0).getString("name")

                            val pokemon = Pokemon(name)
                            textView.text = pokemon.name
                        }
                    }
                }
            })
        } catch (e: IOException) {
            println("Ошибка подключения: $e");
        }
    }
}

class Pokemon(
    val name: String
)

