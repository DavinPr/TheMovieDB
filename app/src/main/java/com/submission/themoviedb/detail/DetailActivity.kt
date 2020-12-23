package com.submission.themoviedb.detail

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
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
import com.submission.themoviedb.databinding.ActivityDetailBinding
import com.submission.themoviedb.helper.barSetup
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val detailViewModel: DetailViewModel by viewModel()
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        barSetup(window, applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.detail_container)
        ) { _, insets ->
            val menuLayoutParams = binding.detailBtnBack.layoutParams as ViewGroup.MarginLayoutParams
            menuLayoutParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            binding.detailBtnBack.layoutParams = menuLayoutParams
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
                    ComponentSetup.setSnackbar(getString(R.string.cast_error_value), binding.detailBottomContainer.rvCast)
                }
            }
        }

        with(binding.detailBottomContainer.rvCast) {
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
                    binding.detailBtnFavorite.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ComponentSetup.setSnackbar(
                                getString(R.string.add_favorite_message),
                                binding.detailBottomContainer.rvCast
                            )
                            favoriteData?.let { detailViewModel.insertFavorite(it) }
                        } else {
                            ComponentSetup.setSnackbar(
                                getString(R.string.remove_favorite_message),
                                binding.detailBottomContainer.rvCast
                            )
                            favoriteData?.let { detailViewModel.deleteFavorite(it) }
                        }
                    }
                }
                is Resource.Error -> {
                    ComponentSetup.setSnackbar(getString(R.string.error_value), binding.detailBottomContainer.rvCast)
                }
            }
        }
        binding.detailBtnBack.setOnClickListener {
            finish()
        }
    }

    private fun setFavorite(state: Boolean) {
        binding.detailBtnFavorite.isChecked = state
    }

    private fun showDetailMovie(detailMovie: DetailMovie?) {
        detailMovie?.let { detail ->
            binding.detailTitle.text = detail.title
            binding.detailTopContainer.detailDate.text = ComponentSetup.dateFormat(detail.release_date)
            binding.detailTopContainer.detailRuntime.text = ComponentSetup.runtimeFormat(detail.runtime)
            val listGenre = ArrayList<String>()
            detail.genreMovie?.map {
                listGenre.add(it.name)
            }
            binding.detailTopContainer.detailGenre.text = listGenre.toString()
            binding.detailOverview.text = detail.overview
            binding.detailBottomContainer.detailRatingbar.apply {
                numStars = 10
                rating = detail.vote_average.toFloat()
                stepSize = .1f
            }
            binding.detailBottomContainer.detailRatingValue.text = detail.vote_average.toString()
            binding.detailBottomContainer.detailPopularityBar.apply {
                max = ComponentSetup.setMaxPopularity(detail.popularity.toInt(), 500)
                progress = detail.popularity.toInt()
            }
            binding.detailBottomContainer.detailPopularityValue.text = detail.popularity.toString()
            detail.poster_path?.let { ComponentSetup.loadImage(this, it, binding.detailPoster) }
            detail.backdrop_path?.let { ComponentSetup.loadImage(this, it, binding.detailBackdrop) }
        }
    }
}