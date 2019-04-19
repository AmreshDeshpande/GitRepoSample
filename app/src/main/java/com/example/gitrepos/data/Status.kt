package com.example.gitrepos.data

import com.example.gitrepos.network.model.ErrorResponse
import com.example.gitrepos.network.model.GitRepo

sealed class Data {

    data class Success(val gitData: List<GitRepo>?) : Data()

    data class Error(val error: ErrorResponse) : Data()

    object Loading : Data()
}