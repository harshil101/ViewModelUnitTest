package com.lab49.taptosnap.network.api

import com.lab49.taptosnap.network.models.ImageRequest
import com.lab49.taptosnap.network.models.QuizQuestionResponse
import com.lab49.taptosnap.network.models.QuizQuestionResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("list")
    suspend fun fetchQuizQuestions(): List<QuizQuestionResponseItem>

    @POST("image")
    suspend fun sendQuizRequest(@Body imageRequest: ImageRequest): QuizQuestionResponse
}