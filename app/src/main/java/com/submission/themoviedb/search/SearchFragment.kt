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
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.R
import com.submission.themoviedb.adapter.SearchListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            val searchAdapter = SearchListAdapter()
            search_view.apply {
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
                                    is Resource.Loading -> search_progressbar.visibility =
                                        View.VISIBLE
                                    is Resource.Success -> {
                                        search_progressbar.visibility = View.GONE
                                        searchAdapter.setData(result.data)
                                    }
                                    is Resource.Error -> {
                                        search_progressbar.visibility = View.GONE
                                        ComponentSetup.setSnackbar(getString(R.string.error_value), rv_trending)
                                    }
                                }
                            }
                        }
                        return true
                    }
                })
            }
            with(rv_search) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = searchAdapter
            }
        }
    }

}