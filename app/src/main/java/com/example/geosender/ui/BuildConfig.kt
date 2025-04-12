package com.example.geosender.ui

import java.io.File
import java.io.FileInputStream

// app/src/main/java/your/package/name/BuildConfig.kt
object BuildConfig {
    private const val MAPS_API_KEY = "MAPS_API_KEY"

    fun getMapsApiKey(): String {
        return try {
            val properties = java.util.Properties()
            properties.load(FileInputStream(File("local.properties")))
            properties.getProperty(MAPS_API_KEY)
        } catch (e: Exception) {
            ""
        }
    }
}