package com.submission.themoviedb.detail.attribute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.databinding.FragmentDetailAttributeBinding

class DetailAttributeFragment : Fragment() {

    private var _binding: FragmentDetailAttributeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailAttributeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val overview = arguments?.getString(EXTRA_OVERVIEW)
        val voteAverage = arguments?.getDouble(EXTRA_RATING, 0.0)
        val popularity = arguments?.getDouble(EXTRA_POPULARITY, 0.0)

        binding.detailOverview.text = overview
        binding.detailRatingbar.apply {
            numStars = 10
            if (voteAverage != null) {
                rating = voteAverage.toFloat()
            }
            stepSize = .1f
        }
        binding.detailRatingValue.text = voteAverage.toString()
        binding.detailPopularityBar.apply {
            if (popularity != null) {
                max = ComponentSetup.setMaxPopularity(popularity.toInt(), 500)
                progress = popularity.toInt()
            }
        }
        binding.detailPopularityValue.text = popularity.toString()
    }

    companion object {
        const val EXTRA_RATING = "rating"
        const val EXTRA_POPULARITY = "popularity"
        const val EXTRA_OVERVIEW = "overview"
    }
}