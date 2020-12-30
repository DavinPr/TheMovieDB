package com.submission.themoviedb.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.submission.core.data.Resource
import com.submission.themoviedb.R
import com.submission.themoviedb.adapter.SearchListAdapter
import com.submission.themoviedb.databinding.SearchFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            val searchAdapter = SearchListAdapter()
            binding.searchView.apply {
                isIconified = false
                onActionViewExpanded()
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if (query.isNullOrEmpty()) {
                            binding.viewError.root.visibility = View.GONE
                            searchAdapter.setData(listOf())
                        } else {
                            lifecycleScope.launch {
                                searchViewModel.queryChannel.send(query)
                            }
                            searchViewModel.searchResult.observe(viewLifecycleOwner) { result ->
                                when (result) {
                                    is Resource.Loading -> binding.searchProgressBar.visibility =
                                        View.VISIBLE
                                    is Resource.Success -> {
                                        binding.searchProgressBar.visibility = View.GONE
                                        if (result.data?.isNotEmpty() == true) {
                                            binding.rvSearch.visibility = View.VISIBLE
                                            binding.viewError.root.visibility = View.GONE
                                            searchAdapter.setData(result.data)
                                        } else {
                                            searchAdapter.setData(listOf())
                                            binding.viewError.tvError.text = context.getString(R.string.not_found)
                                            binding.viewError.imgError.apply {
                                                cropToPadding = true
                                                Glide.with(context)
                                                    .load(ContextCompat.getDrawable(context, R.drawable.illus_empty))
                                                    .into(this)
                                            }
                                            binding.viewError.root.visibility = View.VISIBLE
                                        }
                                    }
                                    is Resource.Error -> {
                                        binding.searchProgressBar.visibility = View.GONE
                                        binding.viewError.root.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                        return true
                    }
                })
            }
            with(binding.rvSearch) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = searchAdapter
            }
        }
    }
}