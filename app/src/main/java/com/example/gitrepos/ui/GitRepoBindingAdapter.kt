package com.example.gitrepos.ui

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.example.gitrepos.data.Data


@BindingAdapter(value = ["dataState", "swipeToRefresh"], requireAll = false)
fun setStateForLoading(progressBar: ProgressBar, data: Data, swipeRefreshLayout: SwipeRefreshLayout) {

    progressBar.visibility = when (data) {

        Data.Loading -> {
            if (!swipeRefreshLayout.isRefreshing) {
                View.VISIBLE
            } else
                View.GONE
        }
        else -> View.GONE
    }
}
