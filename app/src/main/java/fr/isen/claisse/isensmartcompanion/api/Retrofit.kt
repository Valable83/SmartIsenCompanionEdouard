package fr.isen.claisse.isensmartcompanion.api

import fr.isen.claisse.isensmartcompanion.Composables.Event
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}



object RetrofitInstance {
    private const val BASE_EVENTS_URL =
        "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"
    private const val BASE_OPEN_METEO_URL = "https://api.open-meteo.com/"

    val api: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_EVENTS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }

}
