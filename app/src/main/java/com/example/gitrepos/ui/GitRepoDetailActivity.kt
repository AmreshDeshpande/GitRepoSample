package com.example.gitrepos.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.example.gitrepos.R
import com.example.gitrepos.network.model.GitRepo
import com.example.gitrepos.util.Constants.Companion.GIT_REPO_DATA_KEY
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_git_repo_details.*


class GitRepoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_repo_details)
        setSupportActionBar(toolbar)

        showRepoDetails()

    }

    private fun showRepoDetails() {

        val gitRepo = intent?.extras?.get(GIT_REPO_DATA_KEY) as? GitRepo

        gitRepo?.let { gitRepoModel ->

            detailsGitRepoUserImage.let { imageView ->
                Picasso.with(this)
                    .load(gitRepoModel.owner?.avatarUrl)
                    .into(imageView)
            }
            detailsGitRepoDiscription.text = gitRepoModel.description
        }

    }

}
