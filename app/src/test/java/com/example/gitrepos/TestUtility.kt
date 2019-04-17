package com.example.gitrepos

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.util.Log
import com.example.gitrepos.network.model.GitRepo
import com.example.gitrepos.network.model.Owner

class TestUtility {

    companion object {

        fun getTestGitRepoData(): List<GitRepo> {
            return listOf(
                GitRepo(
                    "grit",
                    "**Grit is no longer maintained. Check out libgit2/rugged.** Grit gives you object oriented read/write access to Git repositories via Ruby.",
                    Owner("https://api.github.com/users/mojombo")
                )
            )
        }
    }
}

class TestObserver<T> : Observer<T> {

    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
        Log.d("observedValues", observedValues.toString())
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}