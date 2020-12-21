package com.submission.themoviedb.detail

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.core.data.Resource
import com.submission.core.domain.usecase.model.DetailMovie
import com.submission.core.utils.ComponentSetup
import com.submission.core.utils.DataMapper
import com.submission.themoviedb.R
import com.submission.themoviedb.adapter.CastListAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_data_bottom_container.*
import kotlinx.android.synthetic.main.detail_data_top_container.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setDecorFitsSystemWindows(false)
                window.insetsController?.let {
                    it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            }
            statusBarColor = Color.TRANSPARENT
        }
        

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.detail_container)
        ) { _, insets ->
            val menuLayoutParams = detail_btn_back.layoutParams as ViewGroup.MarginLayoutParams
            menuLayoutParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            detail_btn_back.layoutParams = menuLayoutParams
            insets.consumeSystemWindowInsets()
        }

        val id = intent.getIntExtra(EXTRA_DATA, 0)
        detailViewModel.stateFavorite(id).observe(this) { state ->
            setFavorite(state)
        }

        val castAdapter = CastListAdapter()
        detailViewModel.detailCast(id).observe(this) { cast ->
            when (cast) {
                is Resource.Loading -> {
                    Log.d("cast", "loading")
                }
                is Resource.Success -> {
                    Log.d("cast", cast.data.toString())
                    castAdapter.setData(cast.data)
                }
                is Resource.Error -> {
                    Log.d("cast", "error")
                    ComponentSetup.setSnackbar(getString(R.string.cast_error_value), rv_trending)
                }
            }
        }

        with(rv_cast) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = castAdapter
        }

        detailViewModel.detailData(id).observe(this) { detail ->
            when (detail) {
                is Resource.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    showDetailMovie(detail.data)
                    val favoriteData = detail.data?.let {
                        DataMapper.mapDetailMovieToFavoriteMovie(
                            it
                        )
                    }
                    detail_btn_favorite.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ComponentSetup.setSnackbar(
                                getString(R.string.add_favorite_message),
                                detail_bottom_container
                            )
                            favoriteData?.let { detailViewModel.insertFavorite(it) }
                        } else {
                            ComponentSetup.setSnackbar(
                                getString(R.string.remove_favorite_message),
                                detail_bottom_container
                            )
                            favoriteData?.let { detailViewModel.deleteFavorite(it) }
                        }
                    }
                }
                is Resource.Error -> {
                    ComponentSetup.setSnackbar(getString(R.string.error_value), rv_trending)
                }
            }
        }
        detail_btn_back.setOnClickListener {
            finish()
        }
    }

    private fun setFavorite(state: Boolean) {
        detail_btn_favorite.isChecked = state
    }

    private fun showDetailMovie(detailMovie: DetailMovie?) {
        detailMovie?.let { detail ->
            detail_title.text = detail.title
            detail_date.text = ComponentSetup.dateFormat(detail.release_date)
            detail_runtime.text = ComponentSetup.runtimeFormat(detail.runtime)
            val listGenre = ArrayList<String>()
            detail.genreMovie?.map {
                listGenre.add(it.name)
            }
            detail_genre.text = listGenre.toString()
            detail_overview.text = detail.overview
            detail_ratingbar.apply {
                numStars = 10
                rating = detail.vote_average.toFloat()
                stepSize = .1f
            }
            detail_rating_value.text = detail.vote_average.toString()
            detail_popularity_bar.apply {
                max = ComponentSetup.setMaxPopularity(detail.popularity.toInt(), 500)
                progress = detail.popularity.toInt()
            }
            detail_popularity_value.text = detail.popularity.toString()
            detail.poster_path?.let { ComponentSetup.loadImage(this, it, detail_poster) }
            detail.backdrop_path?.let { ComponentSetup.loadImage(this, it, detail_backdrop) }
        }
    }
}