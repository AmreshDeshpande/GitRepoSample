package com.example.gitrepos.network.model

import com.example.gitrepos.util.Constants.Companion.ERROR_MESSAGE
import com.google.gson.JsonParser
import retrofit2.HttpException

class ErrorResponse constructor(throwable: Throwable) {

    var errorMessage: String

    init {
        errorMessage = when (throwable) {
            is HttpException -> {
                val errorJsonString = throwable.response().errorBody()?.string()
                JsonParser()
                    .parse(errorJsonString)
                    .asJsonObject["message"]
                    .toString()
            }
            else -> throwable.message ?: ERROR_MESSAGE
        }
    }
}