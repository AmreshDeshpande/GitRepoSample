package com.example.gitrepos.util

import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnackBar(message: String){
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG);
    snack.show()
}
