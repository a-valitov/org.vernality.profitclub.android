package org.vernality.profitclub.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_my_organizations_list.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.interactors.MainInteractor
import org.vernality.profitclub.model.data.*
import org.vernality.profitclub.model.repository.RepositoryImplementation
import org.vernality.profitclub.utils.DataSaver
import org.vernality.profitclub.view.activities.EnterRoleActivity
import org.vernality.profitclub.view.activities.MainActivity
import org.vernality.profitclub.view.activities.OnBackPresser
import org.vernality.profitclub.view.adapters.MyOrganizationsListRVAdapter
import org.vernality.profitclub.view.adapters.MyRolesListDataAdapter
import org.vernality.profitclub.view.member.MEMBER
import org.vernality.profitclub.view.member.MemberActivity
import org.vernality.profitclub.view.organization.ORGANIZATION
import org.vernality.profitclub.view.organization.OrganizationActivity
import org.vernality.profitclub.view.supplier.SUPPLIER
import org.vernality.profitclub.view.supplier.SupplierActivity
import org.vernality.profitclub.view_model.MyOrganizationsListFragmentViewModel
import java.lang.reflect.Field


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class DataProcessingFragment : Fragment(), OnBackPressedListener {

    val compos = CompositeDisposable()

    private val repo: RepositoryImplementation by inject<RepositoryImplementation>()

    val interactor: MainInteractor by inject()

    lateinit var exitIV: ImageView
    lateinit var addIV: ImageView
    lateinit var settingsIV: ImageView
    lateinit var popupMenu:PopupMenu
    lateinit var layoutOrganizationsList: LinearLayout
    lateinit var layoutSuppliersList: LinearLayout
    lateinit var layoutMembersList: LinearLayout
    lateinit var layoutPlaceSnack: CoordinatorLayout


    private val viewModel by viewModel<MyOrganizationsListFragmentViewModel>()
    private val adapter: MyOrganizationsListRVAdapter by lazy { MyOrganizationsListRVAdapter(onListItemClickListener) }

    private lateinit var liveDataForLogOutResult: LiveData<AppState>

    private val observer = Observer<AppState> { renderData(it) }
    private val observerForLogOutResult = Observer<AppState> { renderDataForLogOut(it) }

    private lateinit var errorResultDialog: ErrorResultDialogFragment
    private lateinit var loadingLayout: FrameLayout

    private lateinit var rv: RecyclerView




    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val onListItemClickListener: MyOrganizationsListRVAdapter.OnListItemClickListener =
        object : MyOrganizationsListRVAdapter.OnListItemClickListener {
            override fun onItemClick(organization: Organization) {
                Toast.makeText(requireActivity(),organization.name, Toast.LENGTH_SHORT).show()
                viewModel.setOrganization(organization)
                navigateTo()
            }
        }

    private val onOrganizationListItemClickListener: MyRolesListDataAdapter.OnListItemClickListener<Organization> =
        object : MyRolesListDataAdapter.OnListItemClickListener<Organization> {
            override fun onItemClick(organization: Organization) {
                DataSaver.setCurrentBusinessRole(organization)
                viewModel.setOrganization(organization)

                val status = OrganizationStatus.approved.name
                if(organization.statusString.equals(status)){
                    showDialogLogAccount(TypeDialogFragment.LogOrgAccount, organization, organization.name, {navigateToMyOrganization(organization)})
                } else{
                    showInfoDialog(organization, organization.name)
                }
/
            }
        }

    private val onSupplierListItemClickListener: MyRolesListDataAdapter.OnListItemClickListener<Supplier> =
        object : MyRolesListDataAdapter.OnListItemClickListener<Supplier> {
            override fun onItemClick(supplier: Supplier) {
                DataSaver.setCurrentBusinessRole(supplier)
                val status = OrganizationStatus.approved.name
                if(supplier.statusString.equals(status)){
                    showDialogLogAccount(TypeDialogFragment.LogOrgAccount, supplier, supplier.name, {navigateToMySupplier(supplier)})
                } else{
                    showInfoDialog(supplier, supplier.name)
                }


            }
        }

    private val onMemberListItemClickListener: MyRolesListDataAdapter.OnListItemClickListener<Member> =
        object : MyRolesListDataAdapter.OnListItemClickListener<Member> {
            override fun onItemClick(member: Member) {
                DataSaver.setCurrentBusinessRole(member)

                val status = OrganizationStatus.approved.name
                if(member.statusString.equals(status)){
                    showDialogLogAccount(TypeDialogFragment.LogOrgAccount, member, member.name, {navigateToMyMember(member)})
                } else{
                    showInfoDialog(member, member.name)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_my_organizations_list, container, false)

        init(root)

        viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)

        return root
    }


    @SuppressLint("ResourceType")
    fun init(root:View){
        loadingLayout = root.loading_frame_layout
        addIV = root.iv_add
        settingsIV = root.iv_more

        layoutOrganizationsList = root.layout_OrganizationsList
        layoutSuppliersList = root.layout_SupplersList
        layoutMembersList = root.layout_MembersList
//        rv = root.rv_myOrganizations_list
//        rv.layoutManager = LinearLayoutManager(requireActivity())
        layoutPlaceSnack = root.place_snack

        initPopupMenu(settingsIV)

        addIV.setOnClickListener {
            val intent = Intent(requireActivity(), EnterRoleActivity::class.java)
            startActivity(intent)
//            Toast.makeText(requireActivity(), "add clicked", Toast.LENGTH_LONG).show()
        }
        settingsIV.setOnClickListener {

            val menuHelper: Any
            val argTypes: Array<Class<*>?>
            try {
                val fMenuHelper: Field =
                    PopupMenu::class.java.getDeclaredField("mPopup")
                fMenuHelper.setAccessible(true)
                menuHelper = fMenuHelper.get(popupMenu)
                argTypes = arrayOf(Boolean::class.javaPrimitiveType)
                menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
                    .invoke(menuHelper, true)
                menuHelper.javaClass.getDeclaredMethod("setLayoutGravity", *argTypes)
                    .invoke(menuHelper, Gravity.END)
            } catch (e: Exception) {
            }
            popupMenu.show()
        }

    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val data  = appState.data as MyOrganizationsListFragmentViewModel.MyOrganizationsData
                val adapter = MyRolesListDataAdapter(
                    LayoutInflater.from(requireContext()),
                    layoutOrganizationsList,
                    R.layout.item_processing_list_recyclerview,
                    layoutSuppliersList,
                    R.layout.item_processing_list_recyclerview,
                    layoutMembersList,
                    R.layout.item_list_member_short,
                    data,
                    onOrganizationListItemClickListener,
                    onSupplierListItemClickListener,
                    onMemberListItemClickListener
                )
                adapter.inflateViews()

//                val data  = appState.data as List<Organization>
//                rv.adapter = adapter
//                adapter.setData(data = data)

            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = appState.error.message.toString())
//                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }

    private fun renderDataForLogOut(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)

            }
            is AppState.Loading -> {
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


    private fun initPopupMenu(view: View){
        val wrapper = ContextThemeWrapper(requireActivity(), R.style.AppTheme_PopupOverlay)
        popupMenu = PopupMenu(wrapper, view)
        popupMenu.inflate(R.menu.main_toolbar_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_menu_exit -> {
//                    Toast.makeText(requireActivity(), "exit clicked", Toast.LENGTH_LONG).show()
                    viewModel.getLiveDataAndStartGetResultForLogOut().observe(requireActivity(), observerForLogOutResult)
                    true
                }
                else -> false
            }
        }

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

//

    fun navigateTo(){
        val intent = Intent(requireActivity(), OrganizationActivity::class.java)
        requireActivity().startActivity(intent)

        //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
//        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
    }

    fun navigateToMyOrganization(organization: Organization){
        val intent = Intent(requireActivity(), OrganizationActivity::class.java)
        intent.putExtra(ORGANIZATION, organization)
        requireActivity().startActivity(intent)

    }

    fun navigateToMyMember(member: Member){
        val intent = Intent(requireActivity(), MemberActivity::class.java)
        intent.putExtra(MEMBER, member)
        requireActivity().startActivity(intent)

    }

    fun navigateToMySupplier(supplier: Supplier){
        val intent = Intent(requireActivity(), SupplierActivity::class.java)
        intent.putExtra(SUPPLIER,supplier)
        requireActivity().startActivity(intent)

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

    override fun onBackPressed(onBackPresser: OnBackPresser) {


        val snackbar =
            Snackbar.make(layoutPlaceSnack,"", Snackbar.LENGTH_SHORT)

        snackbar.addCallback(object : Snackbar.Callback(){
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onBackPresser.setIsBackPress()
            }
        })
        snackbar.setText(getString(R.string.back_pressed_retry))
        var view = snackbar.view
        val tv =
            view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.view.setBackground(resources.getDrawable(R.drawable.card_info_lite))
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.setMargins(800)


        snackbar.show()
    }

    fun <T>showInfoDialog(role: T, name: String?){
        val successResultDialogFragment
                = SuccessResultDialogFragment.newInstance(TypeDialogFragment.WaitAdministratorApproval)
        {}
        successResultDialogFragment.setName(name)
        successResultDialogFragment.setRole(role)
        successResultDialogFragment.show(parentFragmentManager, this.toString())
    }

    fun <T> showDialogLogAccount(typeDialogFragment: TypeDialogFragment, role: T, name: String?, actionListener: ()->Unit){
        val successResultDialogFragment
                = SuccessResultDialogFragment.newInstance(TypeDialogFragment.LogOrgAccount, actionListener)
        successResultDialogFragment.setName(name)
        successResultDialogFragment.setRole(role)
        successResultDialogFragment.show(parentFragmentManager, this.toString())
    }
}