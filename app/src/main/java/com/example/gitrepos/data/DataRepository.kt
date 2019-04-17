package com.example.gitrepos.data


import com.example.gitrepos.network.GitRepoService
import com.example.gitrepos.network.model.ErrorResponse
import com.example.gitrepos.network.model.GitRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository(private val gitRepoService: GitRepoService) {

    fun getGitRepo(success: (List<GitRepo>?) -> (Unit), failure: (ErrorResponse) -> (Unit)) {


        gitRepoService.getGitRepo().enqueue(object : Callback<List<GitRepo>?> {

            override fun onResponse(call: Call<List<GitRepo>?>, response: Response<List<GitRepo>?>) {
                success.invoke(response.body())
            }

            override fun onFailure(call: Call<List<GitRepo>?>, throwable: Throwable) {
                failure.invoke(ErrorResponse(throwable))
            }

        })
    }

}