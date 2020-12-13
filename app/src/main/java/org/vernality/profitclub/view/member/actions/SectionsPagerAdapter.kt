package org.vernality.profitclub.view.member.actions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.vernality.profitclub.R


private val TAB_TITLES = arrayOf(
        R.string.current_stocks,
        R.string.past_stocks
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter( fm: Fragment)
    : FragmentStateAdapter(fm) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return ActionsPlaceholderFragment.newInstance(position)
    }

}