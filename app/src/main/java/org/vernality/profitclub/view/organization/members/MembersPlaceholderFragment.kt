package org.vernality.profitclub.view.organization.members

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
import kotlinx.android.synthetic.main.list_layout.view.loading_frame_layout
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.view.fragments.ErrorResultDialogFragment
import org.vernality.profitclub.view.fragments.SuccessResultDialogFragment
import org.vernality.profitclub.view.fragments.TypeDialogFragment
import org.vernality.profitclub.view.organization.adapter.MembersListRVAdapter
import org.vernality.profitclub.view.organization.adapter.MembersRequestListRVAdapter


/**
 * A placeholder fragment containing a simple view.
 */
class MembersPlaceholderFragment : Fragment() {

    private var page:Int = 0

    private val viewModel by viewModel<MembersPageViewModel>()

    private val observer = Observer<AppState> { renderData(it) }
    private val observerForMemberResultBtn = Observer<AppState> { renderDataForOfferResultBtn(it) }
    private lateinit var liveDataForMemberResult: LiveData<AppState>

    private lateinit var successResultDialog:SuccessResultDialogFragment
    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private val membersAdapter: MembersListRVAdapter by
    lazy { MembersListRVAdapter(onListItemClickListener) }

    private val membersRequestAdapter: MembersRequestListRVAdapter by
    lazy { MembersRequestListRVAdapter(onListItemClickListenerForRequest, approveBtnListener, rejectBtnListener) }

    private lateinit var rv: RecyclerView

    private val onListItemClickListener: MembersListRVAdapter.OnListItemClickListener =
        object : MembersListRVAdapter.OnListItemClickListener {
            override fun onItemClick(member: Member) {
                println("-----stock on clicked")
                Toast.makeText(requireActivity(), member.firstName, Toast.LENGTH_SHORT).show()
            }
        }

    private val onListItemClickListenerForRequest: MembersRequestListRVAdapter.OnListItemClickListener =
        object : MembersRequestListRVAdapter.OnListItemClickListener {
            override fun onItemClick(member: Member) {
                println("-----stock on clicked")
                Toast.makeText(requireActivity(), member.firstName, Toast.LENGTH_SHORT).show()
            }
        }

    private val  approveBtnListener: MembersRequestListRVAdapter.OnListItemClickListener =
        object : MembersRequestListRVAdapter.OnListItemClickListener {
            override fun onItemClick(member: Member) {
                Toast.makeText(requireActivity(), "approve " + member.firstName, Toast.LENGTH_SHORT).show()
                successResultDialog =
                    SuccessResultDialogFragment.newInstance(
                        TypeDialogFragment.ApproveAction
                    ) {
                        liveDataForMemberResult = viewModel.getLiveDataForResultMember(0, member)
                        liveDataForMemberResult.observe(requireActivity(), observerForMemberResultBtn)
                        successResultDialog.dismiss()

                    }

                successResultDialog.setName(member.firstName +" "+member.lastName)

                successResultDialog.show(parentFragmentManager, this.toString())
            }
        }

    private val rejectBtnListener: MembersRequestListRVAdapter.OnListItemClickListener =
        object : MembersRequestListRVAdapter.OnListItemClickListener {
            override fun onItemClick(member: Member) {
                Toast.makeText(requireActivity(), "reject " + member.firstName, Toast.LENGTH_SHORT).show()
                liveDataForMemberResult = viewModel.getLiveDataForResultMember(1, member)
                liveDataForMemberResult.observe(requireActivity(), observerForMemberResultBtn)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.list_layout, container, false)

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

    private fun initMembersFragment(data: List<Member>){
        membersAdapter.setData(data)
        rv.adapter = membersAdapter
    }

    private fun initRequestsFragment(data: List<Member>){
        membersRequestAdapter.setData(data)
        rv.adapter = membersRequestAdapter
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val data  = appState.data as List<Member>
                when(page){
                    0 -> initMembersFragment(data)
                    1 -> initRequestsFragment(data)
                }
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    private fun renderDataForOfferResultBtn(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                Toast.makeText(requireActivity(), "Succes", Toast.LENGTH_LONG).show()
                liveDataForMemberResult.removeObservers(requireActivity())


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
        fun newInstance(sectionNumber: Int): MembersPlaceholderFragment {
            return MembersPlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
                println("-----PlaceHolderFragment newInstance()")
            }
        }
    }
}