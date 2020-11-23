package org.vernality.profitclub.view.organization.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_members_container.view.*
import kotlinx.android.synthetic.main.fragment_stocks_container.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.view.organization.stocks.SectionsPagerAdapter
import org.vernality.profitclub.view.organization.stocks.StocksContainerFragment


class MembersContainerFragment : Fragment() {

    companion object {
        val TAB_TITLES_MEMBERS = arrayOf(
            R.string.membersOfOrganization,
            R.string.requests
        )
    }

    private lateinit var dashboardViewModel: MembersContainerFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(MembersContainerFragmentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_members_container, container, false)

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        val sectionsPagerAdapter = MembersSectionsPagerAdapter(this)
        val viewPager: ViewPager2 = root.viewpager_members
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = root.sliding_tabs_members
        TabLayoutMediator(
            tabs, viewPager
        ) { tab, position -> tab.setText(TAB_TITLES_MEMBERS[position]) }.attach()
        return root
    }
}