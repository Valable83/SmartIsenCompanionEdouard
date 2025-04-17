package fr.isen.colard.isensmartcompanion.api

import fr.isen.colard.isensmartcompanion.Composables.Event
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}

interface OpenMeteoApi {
    @GET("v1/forecast")
    fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m"
    ): Call<WeatherResponse>
}

object RetrofitInstance {
    private const val BASE_EVENTS_URL = "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"
    private const val BASE_OPEN_METEO_URL = "https://api.open-meteo.com/"

    val api: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_EVENTS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }

    val openMeteoApi: OpenMeteoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_OPEN_METEO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }
}
