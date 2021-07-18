package com.astru43.youtube_checker.ui.main.management

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.astru43.youtube_checker.R
import com.astru43.youtube_checker.ui.main.PlaceholderFragment

private val TAB_TITLES = arrayOf(
    R.string.management_tab_text_1,
    R.string.management_tab_text_2
)

class ManagementPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return PlaceholderFragment.newInstance(0)
            }
            1 -> {
                return SaveFragment.newInstance()
            }
        }
        return PlaceholderFragment.newInstance(-1)
    }


    fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getItemCount(): Int {
        //Return total page count
        return TAB_TITLES.size
    }
}