package com.example.gitrepos.data

import com.example.gitrepos.network.model.ErrorResponse
import com.example.gitrepos.network.model.GitRepo

sealed class Status {

    data class Success(val gitData: List<GitRepo>?) : Status()

    data class Error(val error: ErrorResponse) : Status()

    object Loading : Status()
}