package org.smarts.smartbeerstore.data.weather

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WeatherRepository {
    private val api: WeatherApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(WeatherApi::class.java)
    }

    suspend fun getWeather(city: String): WeatherData {
        try {
            val response = api.getWeather(
                city = city,
                apiKey = "157e41b7d4e4241684ad6eda1a3d9209"
            )
            
            return WeatherData(
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                icon = response.weather.firstOrNull()?.icon ?: ""
            )
        } catch (e: UnknownHostException) {
            throw IOException("No internet connection. Please check your network settings.")
        } catch (e: SocketTimeoutException) {
            throw IOException("Connection timeout. Please try again.")
        } catch (e: Exception) {
            throw IOException("Error fetching weather data: ${e.message}")
        }
    }
} 