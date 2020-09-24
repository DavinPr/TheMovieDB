package com.submission.themoviedb.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.core.data.Resource
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.R
import com.submission.themoviedb.adapter.DiscoverListAdapter
import com.submission.themoviedb.adapter.TrendingListAdapter
import com.submission.themoviedb.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val discoverAdapter = DiscoverListAdapter()
            discoverAdapter.onClickItem = { id ->
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, id)
                startActivity(intent)
            }
            val trendingAdapter = TrendingListAdapter()
            trendingAdapter.onClickItem = { id ->
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, id)
                startActivity(intent)
            }

            homeViewModel.movieDiscover.observe(viewLifecycleOwner) { discover ->
                if (discover != null) {
                    when (discover) {
                        is Resource.Loading -> home_progressbar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            home_progressbar.visibility = View.GONE
                            discoverAdapter.setData(discover.data)
                        }
                        is Resource.Error -> {
                            home_progressbar.visibility = View.GONE
                            ComponentSetup.setSnackbar(getString(R.string.error_value), rv_trending)
                        }
                    }
                }
            }
            homeViewModel.movieTrending.observe(viewLifecycleOwner) { trending ->
                if (trending != null) {
                    when (trending) {
                        is Resource.Success -> trendingAdapter.setData(trending.data)
                        is Resource.Error -> {
                            home_progressbar.visibility = View.GONE
                            ComponentSetup.setSnackbar(getString(R.string.error_value), rv_trending)
                        }
                    }
                }
            }
            with(rv_discover) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = discoverAdapter
            }

            with(rv_trending) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = trendingAdapter
            }

            btn_list_favorite.setOnClickListener {
                val uri = Uri.parse("favoriteapp://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }
}