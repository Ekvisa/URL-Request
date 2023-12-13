package com.example.urlrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    runBlocking {
        try {
            val weatherData = withContext(Dispatchers.IO) {
                weatherApi.getWeatherData(53.198627, 50.113987, "3ef1701acf0ed1315f4a10c81763bc96")
            }

            Log.d("weather log", "Temperature: ${weatherData.main.temp} K")
            Log.d("weather log", "Pressure: ${weatherData.main.pressure} hPa")
            Log.d("weather log", "Humidity: ${weatherData.main.humidity}%")

        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val weatherApi = retrofit.create(WeatherApi::class.java)


interface WeatherApi {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherData
}

data class WeatherData(
    val main: Main
)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main()
    }
}