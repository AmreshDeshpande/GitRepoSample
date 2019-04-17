package com.example.gitrepos

import android.app.Application
import com.example.gitrepos.util.NetworkUtility

class GitRepoApp : Application() {

    init {
        NetworkUtility.init(this)
    }

}