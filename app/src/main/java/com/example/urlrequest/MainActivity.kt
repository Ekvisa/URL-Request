package com.example.urlrequest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun main() {

    runBlocking {

        val response = withContext(Dispatchers.IO) {
            weatherApi.getWeatherData(53.198627, 50.113987, "3ef1701acf0ed1315f4a10c81763bc96")
        }
        if (response.isSuccessful) {
            val weatherMain = response.body()?.main
            if (weatherMain != null) {
                Log.d("weather log", "Temperature: ${weatherMain.temp} K")
                Log.d("weather log", "Pressure: ${weatherMain.pressure} hPa")
                Log.d("weather log", "Humidity: ${weatherMain.humidity}%")
            } else {
                println("Response body is null")
            }
        } else {
            val errorCode = response.code()
            val errorMessage = response.message()
            println("Error: $errorCode, $errorMessage")
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
    ): Response<WeatherData>
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