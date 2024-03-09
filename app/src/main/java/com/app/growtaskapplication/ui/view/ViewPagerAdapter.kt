package com.app.growtaskapplication.ui.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.growtaskapplication.ui.view.AlbumFragment
import com.app.growtaskapplication.ui.view.ArtistFragment
import com.app.growtaskapplication.ui.view.PlaylistFragment
import com.app.growtaskapplication.ui.view.TrackFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ArtistFragment()
            1 -> AlbumFragment()
            2 -> PlaylistFragment()
            3 -> TrackFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}