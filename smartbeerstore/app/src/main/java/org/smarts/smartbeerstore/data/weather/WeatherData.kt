package org.smarts.smartbeerstore.data.weather

data class WeatherData(
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String
) 