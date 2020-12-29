package com.submission.themoviedb.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.submission.core.data.Resource
import com.submission.core.domain.usecase.model.DetailMovie
import com.submission.core.utils.ComponentSetup
import com.submission.core.utils.DataMapper
import com.submission.themoviedb.R
import com.submission.themoviedb.databinding.ActivityDetailBinding
import com.submission.themoviedb.detail.attribute.DetailAttributeFragment
import com.submission.themoviedb.detail.cast.CastDetailFragment
import com.submission.themoviedb.helper.barSetup
import com.submission.themoviedb.helper.blurredImage
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val detailViewModel: DetailViewModel by viewModel()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        barSetup(window, applicationContext, true)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.detail_container)
        ) { _, insets ->
            binding.detailContainer.getConstraintSet(R.id.set1)
                ?.setMargin(R.id.detail_btn_back, ConstraintSet.TOP, insets.systemWindowInsetTop+16)
            binding.detailContainer.getConstraintSet(R.id.set2)
                ?.setMargin(R.id.detail_btn_back, ConstraintSet.TOP, insets.systemWindowInsetTop+16)
            binding.detailContainer.getConstraintSet(R.id.set3)
                ?.setMargin(R.id.detail_btn_back, ConstraintSet.TOP, insets.systemWindowInsetTop+16)
            insets.consumeSystemWindowInsets()
        }



        val id = intent.getIntExtra(EXTRA_DATA, 0)
        detailViewModel.stateFavorite(id).observe(this) { state ->
            setFavorite(state)
        }

        detailViewModel.detailData(id).observe(this) { detail ->
            when (detail) {
                is Resource.Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    showDetailMovie(detail.data)
                    detail.data?.let { setDataBundle(it) }
                        ?.let { sectionsPagerAdapter.setAttributeBundle(it) }
                    detail.data?.let { setCastBundle(it) }
                        ?.let { sectionsPagerAdapter.setCastBundle(it) }

                    val favoriteData = detail.data?.let {
                        DataMapper.mapDetailMovieToFavoriteMovie(
                            it
                        )
                    }
                    binding.detailBtnFavorite.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ComponentSetup.setSnackbar(
                                getString(R.string.add_favorite_message),
                                binding.dataActivityDetail.detailTabLayout
                            )
                            favoriteData?.let { detailViewModel.insertFavorite(it) }
                        } else {
                            ComponentSetup.setSnackbar(
                                getString(R.string.remove_favorite_message),
                                binding.dataActivityDetail.detailTabLayout
                            )
                            favoriteData?.let { detailViewModel.deleteFavorite(it) }
                        }
                    }
                    setTabLayout()

                }
                is Resource.Error -> {
//                    ComponentSetup.setSnackbar(getString(R.string.error_value), binding.detailBottomContainer.rvCast)
                }
            }
        }
        binding.detailBtnBack.setOnClickListener {
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    }

    private fun setFavorite(state: Boolean) {
        binding.detailBtnFavorite.isChecked = state
    }

    private fun showDetailMovie(detailMovie: DetailMovie?) {
        detailMovie?.let { detail ->
            binding.detailTopContainer.detailTitle.text = detail.title
            binding.detailTopContainer.detailDate.text =
                ComponentSetup.dateFormat(detail.release_date, binding.root.context)
            binding.detailTopContainer.detailRuntime.text =
                ComponentSetup.runtimeFormat(detail.runtime)
            val listGenre = ArrayList<String>()
            detail.genreMovie?.map {
                listGenre.add(it.name)
            }
            binding.detailTopContainer.detailGenre.text = listGenre.toString()
            detail.poster_path?.let {
                ComponentSetup.loadImage(
                    this,
                    it,
                    binding.detailPoster
                )
            }
            detail.backdrop_path?.let { blurredImage(this, it, binding.detailBackdrop) }
        }
    }

    private fun setTabLayout() {
        val viewPager: ViewPager = binding.dataActivityDetail.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabLayout: TabLayout = binding.dataActivityDetail.detailTabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setDataBundle(detailMovie: DetailMovie): Bundle {
        return Bundle().apply {
            putString(DetailAttributeFragment.EXTRA_OVERVIEW, detailMovie.overview)
            putDouble(DetailAttributeFragment.EXTRA_RATING, detailMovie.vote_average)
            putDouble(DetailAttributeFragment.EXTRA_POPULARITY, detailMovie.popularity)
        }
    }

    private fun setCastBundle(detailMovie: DetailMovie): Bundle {
        return Bundle().apply {
            putInt(CastDetailFragment.EXTRA_ID, detailMovie.id)
        }
    }
}