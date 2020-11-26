package org.vernality.profitclub.view.organization.supplies

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
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.organization.adapter.SuppliesListRVAdapter
import org.vernality.profitclub.view.organization.stocks.ActionBottomDialogFragment


class SuppliesFragment : Fragment() {


    private val viewModel by viewModel<SuppliesFragmentViewModel>()
    private val adapter: SuppliesListRVAdapter by lazy {SuppliesListRVAdapter(onListItemClickListener)}

    private val observer = Observer<AppState> { renderData(it) }

    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private lateinit var rv: RecyclerView

    private var page:Int = 0

    private val onListItemClickListener: SuppliesListRVAdapter.OnListItemClickListener =
        object : SuppliesListRVAdapter.OnListItemClickListener {
            override fun onItemClick(commercialOffer: CommercialOffer) {
                Toast.makeText(requireActivity(), commercialOffer.message, Toast.LENGTH_SHORT).show()

            }
        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.list_layout, container, false)

        init(root)

        viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)

        return root
    }

    private fun init(root:View){
        loadingLayout = root.loading_frame_layout
        rv = root.rv_list
        rv.layoutManager = LinearLayoutManager(requireActivity())

    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val data  = appState.data as List<CommercialOffer>
                rv.adapter = adapter
                adapter.setData(data = data)

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
}