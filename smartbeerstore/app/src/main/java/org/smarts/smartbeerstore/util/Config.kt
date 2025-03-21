package org.smarts.smartbeerstore.util

import android.content.Context
import java.util.Properties

object Config {
    private var properties: Properties? = null

    fun init(context: Context) {
        properties = Properties().apply {
            context.assets.open("config.properties").use { input ->
                load(input)
            }
        }
    }

    fun getOpenWeatherApiKey(): String {
        return properties?.getProperty("OPENWEATHER_API_KEY") ?: throw IllegalStateException("API key not found. Please add your OpenWeatherMap API key to config.properties")
    }
} 