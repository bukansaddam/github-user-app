package com.saddam.mygithub.ui.detail

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saddam.mygithub.ui.detail.tabLayout.FollowersFragment
import com.saddam.mygithub.ui.detail.tabLayout.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        Log.i(TAG, "username: $username")
        when(position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = Bundle().apply {
            putString(FollowersFragment.EXTRA_USERNAME, username)
            putString(FollowingFragment.EXTRA_USERNAME, username)
        }
        return fragment as Fragment
    }

}