package org.vernality.profitclub.view.organization.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_stocks_container.view.*
import org.vernality.profitclub.R
import timber.log.Timber


class StocksContainerFragment : Fragment() {

    companion object {
        val TAB_TITLES = arrayOf(
            R.string.current_stocks,
            R.string.past_stocks
        )
    }

    private lateinit var homeViewModel: StocksContainerFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView start")
        homeViewModel =
            ViewModelProviders.of(this).get(StocksContainerFragmentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stocks_container, container, false)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)

        val viewPager: ViewPager2 = root.viewpager_stocks
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sectionsPagerAdapter.notifyItemChanged(position)
            }
        })
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = root.sliding_tabs_stocks
        TabLayoutMediator(
            tabs, viewPager
        ) { tab, position -> tab.setText(TAB_TITLES[position]) }.attach()


        homeViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        println("-----onCreateView")
        return root
    }
}