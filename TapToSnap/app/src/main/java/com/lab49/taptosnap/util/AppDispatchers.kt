package com.lab49.taptosnap.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class AppDispatchers(
    val IO: CoroutineDispatcher = Dispatchers.IO,
    val MAIN: CoroutineDispatcher = Dispatchers.Unconfined
)