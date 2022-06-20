package com.lab49.taptosnap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.lab49.taptosnap.network.api.ApiHelper
import com.lab49.taptosnap.network.models.ImageRequest
import com.lab49.taptosnap.util.AppDispatchers
import com.lab49.taptosnap.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    fun getQuizQuestions() = liveData(appDispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.fetchQuestions()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun checkAnswer(imageRequest: ImageRequest) = liveData(appDispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(apiHelper.sendQuizRequest(imageRequest)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
