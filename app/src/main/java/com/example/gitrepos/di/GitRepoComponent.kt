package com.example.gitrepos.di

import com.example.gitrepos.ui.GitRepoActivity
import dagger.Component

@Component(modules = [GitRepoModule::class])
interface GitRepoComponent {

    fun inject(gitRepoActivity: GitRepoActivity)

}