package com.submission.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

object ComponentSetup {
    fun loadImage(context: Context, path: String, view: ImageView) {
        view.cropToPadding = true
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500$path")
            .into(view)
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormat(date: String?): String? {
        if (date == null || date.isEmpty()) return null

        val currentDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val newDateFormat = SimpleDateFormat("MMM dd, yyyy")
        val currentDate = currentDateFormat.parse(date)
        return if (currentDate != null) {
            newDateFormat.format(currentDate)
        } else {
            null
        }
    }

    fun runtimeFormat(runtime: Int): String {
        return if (runtime > 60) {
            val hours = runtime / 60
            val minutes = runtime % 60
            "$hours hours $minutes minutes"
        } else {
            "$runtime minutes"
        }
    }

    fun setMaxPopularity(popularity: Int, max: Int): Int {
        var newMax = max
        while (popularity > newMax) {
            newMax += 500
        }
        return newMax
    }

    fun setSnackbar(message: String, view: View) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}