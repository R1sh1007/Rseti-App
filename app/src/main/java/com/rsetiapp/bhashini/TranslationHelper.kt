package com.rsetiapp.bhashini

import com.google.gson.Gson
import com.rsetiapp.bhashini.RequestModel.Config
import com.rsetiapp.bhashini.RequestModel.InputData
import com.rsetiapp.bhashini.RequestModel.InputItem
import com.rsetiapp.bhashini.RequestModel.Language
import com.rsetiapp.bhashini.RequestModel.PipelineTask
import com.rsetiapp.bhashini.RequestModel.TranslationRequest
import com.rsetiapp.bhashini.ResponseMode.TranslationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TranslationHelper {

    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()

    fun translateText(
        text: String,
        onResult: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()
                val gson = Gson()

                // ✅ Request Model
                val requestModel = TranslationRequest(
                    pipelineTasks = listOf(
                        PipelineTask(
                            taskType = "translation",
                            config = Config(
                                language = Language("en", "en"),
                                serviceId = "ai4bharat/indictrans-v2-all-gpu--t4"
                            )
                        )
                    ),
                    inputData = InputData(
                        input = listOf(InputItem(text))
                    )
                )

                val json = gson.toJson(requestModel)
                val body = json.toRequestBody(mediaType)

                val request = Request.Builder()
                    .url("https://dhruva-api.bhashini.gov.in/services/inference/pipeline")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "5QqK4amQLCINDosdd41kAYmdTKxeQ73js0O8xXtSz_q-T1pmZdKmVZ5ikXOCycLI")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                // ✅ Response Model me convert
                val responseModel = gson.fromJson(
                    responseBody,
                    TranslationResponse::class.java
                )

                val translatedText =
                    responseModel.pipelineResponse[0].output[0].target

                withContext(Dispatchers.Main) {
                    onResult(translatedText)
                }

            } catch (e: Exception) {
                e.printStackTrace()

                withContext(Dispatchers.Main) {
                    onResult(text) // fallback
                }
            }
        }
    }
}