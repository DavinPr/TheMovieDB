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
import com.submission.themoviedb.adapter.DiscoverListAdapter
import com.submission.themoviedb.adapter.TrendingListAdapter
import com.submission.themoviedb.databinding.FragmentHomeBinding
import com.submission.themoviedb.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        is Resource.Loading -> binding.homeProgressbar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.homeProgressbar.visibility = View.GONE
                            discoverAdapter.setData(discover.data)
                        }
                        is Resource.Error -> {
                            binding.homeProgressbar.visibility = View.GONE
                            binding.viewError.root.visibility = View.VISIBLE
                        }
                    }
                }
            }
            homeViewModel.movieTrending.observe(viewLifecycleOwner) { trending ->
                if (trending != null) {
                    when (trending) {
                        is Resource.Loading -> binding.homeProgressbar.visibility = View.VISIBLE
                        is Resource.Success -> trendingAdapter.setData(trending.data)
                        is Resource.Error -> {
                            binding.homeProgressbar.visibility = View.GONE
                            binding.viewError.root.visibility = View.VISIBLE
                        }
                    }
                }
            }
            with(binding.rvDiscover) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = discoverAdapter
            }

            with(binding.rvTrending) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = trendingAdapter
            }

            binding.btnListFavorite.setOnClickListener {
                val uri = Uri.parse("favoriteapp://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }
}