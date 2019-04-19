package com.example.gitrepos.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel


class GitRepoViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val status: MutableLiveData<Status>? = MutableLiveData()

    //Exposing live status to view and not Mutable live status
    fun getGitRepoData(): LiveData<Status>? = status


    fun getGitRepo() {

        status?.value = Status.Loading

        dataRepository.getGitRepo({ gitRepoList ->

            status?.value = Status.Success(gitRepoList)
        },
            { errorResponse ->
                status?.value = Status.Error(errorResponse)
            })
    }

}

