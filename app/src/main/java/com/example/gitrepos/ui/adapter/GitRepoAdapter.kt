package com.example.gitrepos.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gitrepos.R
import com.example.gitrepos.network.model.GitRepo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.git_repo_item.view.*

class GitRepoAdapter(private val itemClick: (GitRepo?) -> (Unit)) : RecyclerView.Adapter<GitRepoViewHolder>() {

    var gitRepoData: List<GitRepo>? = null

    override fun onCreateViewHolder(parentView: ViewGroup, position: Int): GitRepoViewHolder {
        val itemView = LayoutInflater.from(parentView.context).inflate(R.layout.git_repo_item, parentView, false)
        return GitRepoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gitRepoData?.size ?: 0
    }

    override fun onBindViewHolder(gitRepoViewholder: GitRepoViewHolder, position: Int) {
        val gitRepo = gitRepoData?.get(position)
        gitRepoViewholder.bind(gitRepo, itemClick)
    }
}

class GitRepoViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    fun bind(gitRepo: GitRepo?, itemClick: (GitRepo?) -> Unit) {

        itemView.also { view ->
            view.gitRepoUserName.text = gitRepo?.name
            view.gitRepoDiscription.text = gitRepo?.description
        }.also { view ->
            Picasso.with(view.context)
                .load(gitRepo?.owner?.avatarUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_person_outline_black)
                .into(itemView.gitRepoUserImage)
        }.also { view ->
            view.setOnClickListener {
                itemClick.invoke(gitRepo)
            }
        }
    }

}
