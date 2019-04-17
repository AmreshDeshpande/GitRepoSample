package com.example.gitrepos.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.example.gitrepos.data.DataRepository
import com.example.gitrepos.data.GitRepoViewModel
import com.example.gitrepos.network.GitRepoService
import com.example.gitrepos.ui.GitRepoActivity
import com.example.gitrepos.ui.adapter.GitRepoAdapter
import com.example.gitrepos.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class GitRepoModule(private val gitRepoActivity: GitRepoActivity) {

    @Provides
    fun providesRepository(gitRepoService: GitRepoService): DataRepository {
        return DataRepository(gitRepoService)
    }

    @Provides
    fun providesGitRepoViewModel(gitRepoModelFactory: GitRepoModelFactory): GitRepoViewModel {
        return ViewModelProviders
            .of(gitRepoActivity, gitRepoModelFactory)
            .get(GitRepoViewModel::class.java)
    }

    @Provides
    fun providesGitRepoModelFactory(repository: DataRepository): GitRepoModelFactory {
        return GitRepoModelFactory(repository)
    }

    @Provides
    fun providesGitRepoAdapter(): GitRepoAdapter {
        return GitRepoAdapter(gitRepoActivity.itemClick)
    }

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesGitRepoService(retrofit: Retrofit): GitRepoService {
        return retrofit.create(GitRepoService::class.java)
    }
}


class GitRepoModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GitRepoViewModel::class.java)) {
            GitRepoViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}