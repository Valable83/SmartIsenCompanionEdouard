package fr.isen.claisse.isensmartcompanion.api

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double
)
