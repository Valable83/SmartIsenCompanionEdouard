package fr.isen.colard.isensmartcompanion.api

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Gemini {

    private const val API_KEY = "AIzaSyCRpCN5LUn_a-gXe-RND56MfpsJFtK5mHU"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = API_KEY
    )

    suspend fun getGeminiResponse(prompt: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(content { text(prompt) })
                response.text
            } catch (e: Exception) {
                null
            }
        }
    }
}
