package org.vernality.profitclub.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_select_organization_for_member.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import org.vernality.profitclub.view.adapters.ListOrgForMemberRVAdapter
import org.vernality.profitclub.view_model.SelectOrgForMemberFragmentViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class SelectOrgForMemberFragment : Fragment() {

    val compos = CompositeDisposable()

    private val viewModel by viewModel<SelectOrgForMemberFragmentViewModel>()

    lateinit var backCL: ConstraintLayout
    lateinit var userName: TextView
    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var successResultDialog: SuccessResultDialogFragment

    private val observer = Observer<AppState> { renderData(it) }

    private val listObserver = Observer<AppState> { listRenderData(it) }

    lateinit var  adapter: ListOrgForMemberRVAdapter

    private lateinit var root:View

    private val onListItemClickListener: ListOrgForMemberRVAdapter.OnListItemClickListener =
        object : ListOrgForMemberRVAdapter.OnListItemClickListener {
            override fun onItemClick(organization: Organization) {
                Toast.makeText(requireActivity(), "item recycler clicked", Toast.LENGTH_LONG).show()
                viewModel.getLiveDataAndStartGetResult(organization).observe(requireActivity(), observer)
            }
        }

    // TODO: Rename and change types of parameters
    private var firstName: String? = null
    private var lastName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstName = it.getString(MEMBER_FIRST_NAME)
            lastName = it.getString(MEMBER_LAST_NAME)
        }
        val intFirstName = firstName
        val intLastName = lastName
        if(!intFirstName.isNullOrEmpty() && !intLastName.isNullOrEmpty()) viewModel.setName(intFirstName, intLastName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_select_organization_for_member, container, false)

        init(root)

        initResultSendData()

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("ResourceType")
    fun init(root:View){
        backCL = root.back
        userName = root.tv_userName

        userName.setText(viewModel.getUserName())

        backCL.setOnClickListener {
            Toast.makeText(requireActivity(), "add clicked", Toast.LENGTH_LONG).show()
        }

        viewModel.listLiveData.observe(requireActivity(), listObserver)

        viewModel.getOrganizationList()


    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {

                successResultDialog =
                    SuccessResultDialogFragment.newInstance(TypeDialogFragment.RoleApproveAdmin
                    ) { navigateTo() }

                successResultDialog.show(parentFragmentManager, this.toString())
//                val successResultDialog
//                        = SuccessResultDialogFragment.newInstance(TypeDialogFragment.Registration)
//                successResultDialog.apply{
//                    setListenerOnAction({
//                        navigateTo()
//                        dismiss()
//                    })
//                    show(parentFragmentManager, this@SelectOrgForMemberFragment.toString())
//                }
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    private fun listRenderData(appState: AppState) {
        println("into orgForMemberFrag, state = "+appState)
        when (appState) {
            is AppState.Success<*> -> {
                val list = appState.data as List<Organization>
                println("into orgForMemberFrag, Succes, list = "+list)
                adapter = ListOrgForMemberRVAdapter(onListItemClickListener, appState.data)
                root.rv_data_processing.layoutManager = LinearLayoutManager(requireContext())
                root.rv_data_processing.adapter = adapter
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message?:getString(R.string._minus1))
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }


    fun navigateTo(){
        val intent = Intent(requireActivity(), SelectOrganizationActivity::class.java).apply { addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP
        ) }
        startActivity(intent)
    }


    private fun initPopupMenu(view: View){
//        val wrapper = ContextThemeWrapper(requireActivity(), R.style.AppTheme_PopupOverlay)
//        popupMenu = PopupMenu(wrapper, view)
//        popupMenu.inflate(R.menu.main_toolbar_menu)
//        popupMenu.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.item_menu_exit -> {
//                    Toast.makeText(requireActivity(), "exit clicked", Toast.LENGTH_LONG).show()
//                    true
//                }
//                else -> false
//            }
//        }

    }


    private fun initResultSendData(){

    }







    private fun setListenerExit(exit: View) {
        exit.setOnClickListener {
            Toast.makeText(requireActivity(), "exit is checked", Toast.LENGTH_LONG).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        compos.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu)

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectOrgForMemberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}