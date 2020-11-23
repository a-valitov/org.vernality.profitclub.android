package org.vernality.profitclub.view.organization.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.organization.adapter.StocksListRVAdapter


/**
 * A placeholder fragment containing a simple view.
 */
class StocksPlaceholderFragment : Fragment() {

    private val viewModel by viewModel<StocksPageViewModel>()
    private val adapter: StocksListRVAdapter by lazy { StocksListRVAdapter(onListItemClickListener) }

    private val observer = Observer<AppState> { renderData(it) }

    private var page:Int = 0

    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private lateinit var rv: RecyclerView

    private val onListItemClickListener: StocksListRVAdapter.OnListItemClickListener =
        object : StocksListRVAdapter.OnListItemClickListener {
            override fun onItemClick(action: Action) {
                println("-----stock on clicked")
                Toast.makeText(requireActivity(), action.message, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("---onCreate")
        viewModel.apply{
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.list_layout, container, false)
        println("---onCreateView")
        init(root)

        return root
    }

    private fun init(root:View){
        loadingLayout = root.loading_frame_layout
        rv = root.rv_list
        rv.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.page.observe(this, Observer<Int> {
            page = it
            viewModel.getLiveDataAndStartGetResult(page).observe(requireActivity(), observer)
        })
    }

    private fun initCurrentActions(data: List<Action>){
        adapter.setData(data)
        rv.adapter = adapter
    }

    private fun initPastActions(data: List<Action>){
        adapter.setData(data)
        rv.adapter = adapter
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val data  = appState.data as List<Action>
                when(page){
                    0 -> initCurrentActions(data)
                    1 -> initPastActions(data)
                }
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message.toString())
                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): StocksPlaceholderFragment {
            return StocksPlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
                println("-----PlaceHolderFragment newInstance()")
            }
        }
    }
}