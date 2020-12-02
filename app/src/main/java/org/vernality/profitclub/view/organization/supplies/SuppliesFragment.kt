package org.vernality.profitclub.view.organization.supplies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import org.vernality.profitclub.view.organization.adapter.SuppliesListRVAdapter
import org.vernality.profitclub.view.organization.stocks.ActionBottomDialogFragment
import timber.log.Timber


class SuppliesFragment : Fragment() {

    private var page:Int = 0

    private val viewModel by viewModel<SuppliesFragmentViewModel>()

    private val observer = Observer<AppState> { renderData(it) }
    private val observerForOfferResultBtn = Observer<AppState> { renderDataForOfferResultBtn(it) }
    private lateinit var liveDataForOfferResult: LiveData<AppState>

    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingLayout: FrameLayout
    private lateinit var offerBottomDialogFragment:OfferBottomDialogFragment
    private lateinit var successResultDialog:SuccessResultDialogFragment

    private val adapter: SuppliesListRVAdapter by lazy {SuppliesListRVAdapter(onListItemClickListener)}
    private lateinit var rv: RecyclerView

    private val onListItemClickListener: SuppliesListRVAdapter.OnListItemClickListener =
        object : SuppliesListRVAdapter.OnListItemClickListener {
            override fun onItemClick(commercialOffer: CommercialOffer) {
                Toast.makeText(requireActivity(), commercialOffer.message, Toast.LENGTH_SHORT).show()
                offerBottomDialogFragment = (OfferBottomDialogFragment.newInstance(commercialOffer))
                    .apply {
                        setApproveClickListener(approveBtnListener)
                        setRejectClickListener(rejectBtnListener)
                    }
                offerBottomDialogFragment.show(parentFragmentManager, "BOTTOM_SHEET_FRAGMENT_DIALOG_TAG")

            }
        }

    private val approveBtnListener =
        object :OfferBottomDialogFragment.OnOfferClickListener{
            override fun onClick(offer: CommercialOffer) {
                Toast.makeText(requireActivity(),"accept action clicked", Toast.LENGTH_SHORT).show()
                successResultDialog =
                    SuccessResultDialogFragment.newInstance(
                        TypeDialogFragment.ApproveAction
                    ) {
                        liveDataForOfferResult = viewModel.getLiveDataForOffer(0, offer)
                        liveDataForOfferResult.observe(requireActivity(), observerForOfferResultBtn)
                        successResultDialog.dismiss()

                    }

                println("-----supplier name = "+offer.supplier?.contactName)
                successResultDialog.setName(offer.supplier?.contactName)

                successResultDialog.show(parentFragmentManager, this.toString())

            }

        }

    private val rejectBtnListener =
        object :OfferBottomDialogFragment.OnOfferClickListener{
            override fun onClick(offer: CommercialOffer) {
                Toast.makeText(requireActivity(),"reject action clicked", Toast.LENGTH_SHORT).show()
                liveDataForOfferResult = viewModel.getLiveDataForOffer(1, offer)
                liveDataForOfferResult.observe(requireActivity(), observerForOfferResultBtn)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView start")

        val root = inflater.inflate(R.layout.list_layout, container, false)

        init(root)

        viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)
        Timber.d("onCreateView end")
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

    private fun renderDataForOfferResultBtn(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                Toast.makeText(requireActivity(), "Succes", Toast.LENGTH_LONG).show()
                liveDataForOfferResult.removeObservers(requireActivity())
                offerBottomDialogFragment.dismiss()

            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()

            }
            is AppState.Error -> {

                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message.toString())
                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }
}