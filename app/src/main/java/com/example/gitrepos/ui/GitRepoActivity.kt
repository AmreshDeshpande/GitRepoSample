package com.example.gitrepos.ui


import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.gitrepos.data.Status
import com.example.gitrepos.data.GitRepoViewModel
import com.example.gitrepos.databinding.ActivityGitRepoBinding
import com.example.gitrepos.di.DaggerGitRepoComponent
import com.example.gitrepos.di.GitRepoModule
import com.example.gitrepos.network.model.GitRepo
import com.example.gitrepos.ui.adapter.GitRepoAdapter
import com.example.gitrepos.util.Constants.Companion.GIT_REPO_DATA_KEY
import com.example.gitrepos.util.NetworkUtility
import com.example.gitrepos.util.showSnackBar
import kotlinx.android.synthetic.main.activity_git_repo.*
import kotlinx.android.synthetic.main.no_connection.*
import javax.inject.Inject


class GitRepoActivity : AppCompatActivity() {

    @Inject
    lateinit var gitRepoViewModel: GitRepoViewModel

    @Inject
    lateinit var gitRepoAdapter: GitRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerGitRepoComponent
            .builder()
            .gitRepoModule(GitRepoModule(this))
            .build()
            .inject(this)

        setUpUI()

        getGitRepos()

    }

    private fun setUpUI() {

        setDataBinding()

        setSupportActionBar(toolbar)

        setUpRecyclerView()

        setUpSwipeToRefresh()

        setUpNetworkListener()
    }

    private fun setDataBinding() {
        val binding: ActivityGitRepoBinding =
            DataBindingUtil.setContentView(this, com.example.gitrepos.R.layout.activity_git_repo)
        binding.setLifecycleOwner(this)
        binding.gitRepoViewModel = gitRepoViewModel
    }

    private fun setUpRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = gitRepoAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun setUpSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            getGitRepos()
        }
    }

    private fun getGitRepos() {
        
        gitRepoViewModel.getGitRepoData()?.observe(this,
            Observer { status ->

                when (status) {
                    is Status.Success -> {
                        gitRepoAdapter.gitRepoData = status.gitData
                        gitRepoAdapter.notifyDataSetChanged()
                        swipeRefresh.isRefreshing = false
                    }
                    is Status.Error -> {
                        gitRepoCoordinatorLayout.showSnackBar(status.error.errorMessage)
                    }
                }
            })
        gitRepoViewModel.getGitRepo()
    }

    //Handle item click
    var itemClick: (GitRepo?) -> (Unit) = { gitRepo ->
        val intent = Intent(this, GitRepoDetailActivity::class.java)
        intent.putExtra(GIT_REPO_DATA_KEY, gitRepo)
        startActivity(intent)
    }

    private fun setUpNetworkListener() {

        NetworkUtility.registerNetworkCallback()
        NetworkUtility.observe(this, Observer { connection ->
            connection?.let {
                if (!connection) {
                    recyclerView.visibility = View.GONE
                    noConnectionLayout.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    noConnectionLayout.visibility = View.GONE
                }
            }
        })

        tryAgainBtn.setOnClickListener {
            getGitRepos()
        }
    }
}
