package com.cscareer.movieapp.util

import android.content.Context
import android.widget.Toast
import com.cscareer.movieapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun showToast(context: Context) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
    }
}