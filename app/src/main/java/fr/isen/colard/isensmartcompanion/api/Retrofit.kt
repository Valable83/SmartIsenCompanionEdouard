package fr.isen.colard.isensmartcompanion.api

import fr.isen.colard.isensmartcompanion.Composables.Event
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// ✅ Interface de l'API Retrofit
interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}

// ✅ Singleton Retrofit
object RetrofitInstance {
    private const val BASE_URL =
        "https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/"

    val api: EventApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
    }
}
