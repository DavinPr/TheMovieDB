package com.submission.themoviedb.detail.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.core.data.Resource
import com.submission.themoviedb.adapter.CastListAdapter
import com.submission.themoviedb.databinding.FragmentCastDetailBinding
import com.submission.themoviedb.detail.DetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CastDetailFragment : Fragment() {

    private val detailViewModel: DetailViewModel by viewModel()
    private var _binding : FragmentCastDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCastDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(EXTRA_ID)

        val castAdapter = CastListAdapter()
        if (id != null) {
            detailViewModel.detailCast(id).observe(viewLifecycleOwner) { cast ->
                when (cast) {
                    is Resource.Loading -> {
                        Timber.d("Cast Movie Loading")
                    }
                    is Resource.Success -> {
                        castAdapter.setData(cast.data)
                    }
                    is Resource.Error -> {
    //                    ComponentSetup.setSnackbar(getString(R.string.cast_error_value), binding.detailBottomContainer.rvCast)
                    }
                }
            }
        }

        with(binding.rvCast) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}