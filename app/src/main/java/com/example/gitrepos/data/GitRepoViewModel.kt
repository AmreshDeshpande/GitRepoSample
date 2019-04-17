package com.example.gitrepos.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel


class GitRepoViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val data: MutableLiveData<Data>? = MutableLiveData()

    //Exposing live data to view and not Mutable live data
    fun getGitRepoData(): LiveData<Data>? = data


    fun getGitRepo() {

        data?.value = Data.Loading

        dataRepository.getGitRepo({ gitRepoList ->

            data?.value = Data.Success(gitRepoList)
        },
            { errorResponse ->
                data?.value = Data.Error(errorResponse)
            })
    }

}

