package com.submission.themoviedb.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.submission.themoviedb.R
import com.submission.themoviedb.databinding.FragmentAboutBinding


class AboutFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutGithub.setOnClickListener(this)
        binding.aboutGmail.setOnClickListener(this)
        binding.aboutInstagram.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v) {
                binding.aboutGithub -> {
                    val github = getString(R.string.github_link)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(github)))
                }
                binding.aboutGmail -> {
                    val gmail = getString(R.string.gmail_address)
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$gmail"))
                    startActivity(intent)
                }
                binding.aboutInstagram -> {
                    val instagram = getString(R.string.ig_link)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(instagram)))
                }
            }
        }
    }
}