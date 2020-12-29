package com.submission.themoviedb.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.submission.themoviedb.R
import com.submission.themoviedb.detail.attribute.DetailAttributeFragment
import com.submission.themoviedb.detail.cast.CastDetailFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var attributeBundle: Bundle? = null
    private var castBundle: Bundle? = null

    fun setAttributeBundle(bundle: Bundle) {
        this.attributeBundle = bundle
    }

    fun setCastBundle(bundle: Bundle) {
        this.castBundle = bundle
    }

    @StringRes
    private val titles = intArrayOf(R.string.attribute, R.string.cast)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                val detailAttributeFragment = DetailAttributeFragment()
                detailAttributeFragment.arguments = attributeBundle
                fragment = detailAttributeFragment
            }
            1 -> {
                val castDetailFragment = CastDetailFragment()
                castDetailFragment.arguments = castBundle
                fragment = castDetailFragment
            }
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(titles[position])
    }
}