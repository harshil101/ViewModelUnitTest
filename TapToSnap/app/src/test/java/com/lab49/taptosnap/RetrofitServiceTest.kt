package com.lab49.taptosnap

import com.google.gson.Gson
import com.lab49.taptosnap.network.api.ApiService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }


    @Test
    fun `get questions api test`() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(mockResponse.setBody("[]"))
            val response = apiService.fetchQuizQuestions()
            val request = mockWebServer.takeRequest()
            assertEquals("/list", request.path)
            assertEquals(true, response.isEmpty())
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}