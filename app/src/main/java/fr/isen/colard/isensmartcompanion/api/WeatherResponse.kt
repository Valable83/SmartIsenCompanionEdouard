package fr.isen.colard.isensmartcompanion.api

// ✅ Modèle pour l'API Open-Meteo (température actuelle)
data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double
)
