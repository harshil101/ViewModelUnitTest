package com.lab49.taptosnap.network.api

import com.lab49.taptosnap.network.models.ImageRequest
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {
    suspend fun fetchQuestions() = apiService.fetchQuizQuestions()

    suspend fun sendQuizRequest(imageRequest: ImageRequest) = apiService.sendQuizRequest(imageRequest)
}