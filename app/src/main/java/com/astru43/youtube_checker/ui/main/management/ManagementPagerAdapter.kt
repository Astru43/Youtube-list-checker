package com.astru43.youtube_checker.ui.main.management

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.astru43.youtube_checker.R

private val TAB_TITLES = arrayOf(
    R.string.management_tab_text_1,
    R.string.management_tab_text_2
)

class ManagementPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return SaveFragment.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        //Return total page count
        return TAB_TITLES.size
    }
}