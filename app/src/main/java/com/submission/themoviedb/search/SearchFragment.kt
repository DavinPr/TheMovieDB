package com.submission.themoviedb.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.core.data.Resource
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
                        if (query != null) {
                            lifecycleScope.launch {
                                searchViewModel.queryChannel.send(query)
                            }
                            searchViewModel.searchResult.observe(viewLifecycleOwner) { result ->
                                when (result) {
                                    is Resource.Loading -> binding.searchProgressbar.visibility =
                                        View.VISIBLE
                                    is Resource.Success -> {
                                        binding.searchProgressbar.visibility = View.GONE
                                        searchAdapter.setData(result.data)
                                    }
                                    is Resource.Error -> {
                                        binding.searchProgressbar.visibility = View.GONE
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