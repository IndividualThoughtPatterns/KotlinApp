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
import org.json.JSONObject
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView;
import android.view.View
import android.view.LayoutInflater

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient()
        val limit: Int = 20
        val url = "https://pokeapi.co/api/v2/pokemon?limit=$limit"
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
                            val names: List<String>

                            val json = JSONObject(response.body!!.string())
                            val results = json.getJSONArray("results")

                            names = List(results.length()) {
                                results.getJSONObject(it).getString("name")
                            }

                            recyclerView.adapter = PokemonAdapter(names)
                        }
                    }
                }
            })
        } catch (e: IOException) {
            println("Ошибка подключения: $e");
        }
    }
}


