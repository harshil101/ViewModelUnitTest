package com.lab49.taptosnap.util

import com.lab49.taptosnap.network.States

data class Resource<out T>(val status: States, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = States.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = States.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> = Resource(status = States.LOADING, data = data, message = null)
    }
}
