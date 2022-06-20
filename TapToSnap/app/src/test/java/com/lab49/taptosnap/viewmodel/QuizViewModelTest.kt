package com.lab49.taptosnap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lab49.taptosnap.MainCoroutineRule
import com.lab49.taptosnap.network.api.ApiHelper
import com.lab49.taptosnap.network.api.ApiService
import com.lab49.taptosnap.network.models.ImageRequest
import com.lab49.taptosnap.network.models.QuizQuestionResponse
import com.lab49.taptosnap.network.models.QuizQuestionResponseItem
import com.lab49.taptosnap.util.AppDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuizViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = AppDispatchers(
        IO = StandardTestDispatcher(),
        MAIN = Dispatchers.Unconfined
    )

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var apiHelper: ApiHelper
    private var apiUsersObserver: List<QuizQuestionResponseItem> =
        arrayListOf(
            QuizQuestionResponseItem(
                1,
                "box"
            )
        )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        apiHelper = ApiHelper(apiService)
        quizViewModel = QuizViewModel(apiHelper, testDispatcher)
    }

    @Test
    fun givenServerResponse200_whenFetch_questionsList_shouldReturnSuccess() {
        mainCoroutineRule.runBlockingTest {
            `when`(apiService.fetchQuizQuestions()).thenReturn(apiUsersObserver)
            quizViewModel.getQuizQuestions()
            val response = apiHelper.fetchQuestions()
            Assert.assertNotNull(response)
            Assert.assertEquals(
                apiUsersObserver,
                response
            )
        }
    }

    @Test
    fun `get empty list test`() {
        mainCoroutineRule.runBlockingTest {
            `when`(apiService.fetchQuizQuestions()).thenReturn(listOf())
            val response = apiHelper.fetchQuestions()
            Assert.assertEquals(listOf<QuizQuestionResponseItem>(), response)
        }
    }

    @Test
    fun givenServerResponse200_whenFetch_checkAnswer_shouldReturnSuccess() {
        mainCoroutineRule.runBlockingTest {
            val mockImg = mock(ImageRequest::class.java)
           `when`(apiService.sendQuizRequest(mockImg)).thenReturn(
                QuizQuestionResponse(
                    matched = true
                )
            )
            quizViewModel.checkAnswer(mockImg)
            val response = apiHelper.sendQuizRequest(mockImg)
            Assert.assertNotNull(response)
            Assert.assertEquals(true, response.matched)
        }
    }

    @Test
    fun givenServerResponse200_whenFetch_checkAnswer_shouldReturnFail() {
        mainCoroutineRule.runBlockingTest {
            val mockImg = mock(ImageRequest::class.java)
            `when`(apiService.sendQuizRequest(mockImg)).thenReturn(
                QuizQuestionResponse(
                    matched = false
                )
            )
            quizViewModel.checkAnswer(mockImg)
            val response = apiHelper.sendQuizRequest(mockImg)
            Assert.assertNotNull(response)
            Assert.assertEquals(false, response.matched)
        }
    }
}