package org.vernality.profitclub.view.fragments

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_enter_organization.view.*
import kotlinx.android.synthetic.main.fragment_enter_organization.view.loading_frame_layout
import kotlinx.android.synthetic.main.item_enter_organization.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.BaseCompany
import org.vernality.profitclub.view.activities.SelectOrganizationActivity
import org.vernality.profitclub.view_model.*




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [RoleSelectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterOrganizationDataFragment : Fragment() {

    val compos = CompositeDisposable()

    private val viewModel by viewModel<EnterOrganizationDataFragmentViewModel>()

    private val observer = Observer<AppState> { renderData(it) }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var baseCompany: BaseCompany? = null
    private lateinit var loadingLayout: FrameLayout
    private lateinit var successResultDialog: SuccessResultDialogFragment
    private lateinit var errorResultDialog: ErrorResultDialogFragment

    lateinit var sendDataBtn:  MaterialButton
    lateinit var nameOfOrganizationET: TextInputEditText
    lateinit var INNOfOrganizationET: TextInputEditText
    lateinit var FCSET: TextInputEditText
    lateinit var phoneET: TextInputEditText
    lateinit var privacyPolicyTV: TextView
    lateinit var exit: ConstraintLayout
    lateinit var roleTV: TextView
    lateinit var agreeCB: CheckBox
    lateinit var roleHideTV: TextView


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
        val root = inflater.inflate(R.layout.fragment_enter_organization, container, false)

        init(root)

        if (savedInstanceState == null) {
            baseCompany = viewModel.baseCompany
            setFields(baseCompany)
        } else {

        }

        println("------onCreateView Enter")

        return root
    }

    private fun setFields(baseCompany: BaseCompany?) {
        nameOfOrganizationET.setText(baseCompany?.name)
        INNOfOrganizationET.setText(baseCompany?.inn)
        FCSET.setText(baseCompany?.contactName)
        phoneET.setText(baseCompany?.phone)
    }


    fun init(root:View){
        sendDataBtn = root.btn_send
        nameOfOrganizationET= root.et_enter_name_of_organization
        INNOfOrganizationET= root.et_enter_INN
        FCSET= root.et_enter_FCS
        phoneET = root.et_enter_phone
        privacyPolicyTV = root.tv_privacy_policy
        exit = root.exit_enter_role
        roleTV = root.tittle_role
        agreeCB = root.cb_agree
        roleHideTV = root.tv_input_hint

        loadingLayout = root.loading_frame_layout

        setRxToNameOfOrgET(nameOfOrganizationET)
        setRxToINNET(INNOfOrganizationET)
        setRxToFCSET(FCSET)
        setRxToPhoneET(phoneET)

        setListenerExit(exit)
        setListenerSendDataBtn(sendDataBtn)
        setListenerPrivacyPolicy(privacyPolicyTV)
        setListenerAgreeCB(agreeCB)

        setRole()

    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                loadingLayout.visibility = View.GONE
                successResultDialog =
                    SuccessResultDialogFragment.newInstance(TypeDialogFragment.RoleApproveAdmin
                ) { navigateTo() }

                successResultDialog.show(parentFragmentManager, this.toString())
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                errorResultDialog =
                    ErrorResultDialogFragment.newInstance(description = "Не удалось отправить данные в обработку")
                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                errorResultDialog.show(parentFragmentManager, this.toString())
            }
        }
    }




    private fun setRole() {

            when (viewModel.role) {
                Role.Supplier -> {
                    roleTV.setText(R.string.supplier)
                    roleHideTV.setText(R.string.provider_description)

                }
                Role.Organization -> {
                    roleTV.setText(R.string.organization)
                    roleHideTV.setText(R.string.organization_description)

                }
                else -> Throwable("полуена недопустимая роль в фрагменте")
            }

    }



    fun navigateTo(){
        val intent = Intent(requireActivity(), SelectOrganizationActivity::class.java).apply { addFlags(FLAG_ACTIVITY_CLEAR_TOP) }
        startActivity(intent)
    }


    private fun setRxToNameOfOrgET(nameOfOrganizationET: TextInputEditText) {
        val disposableName: Disposable = RxTextView.textChanges(nameOfOrganizationET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setNameOfOrganization(charSequence.toString())
                }
            })

        compos.add(disposableName)
    }

    private fun setRxToINNET(INNET: TextInputEditText) {
        val disposableINN: Disposable = RxTextView.textChanges(INNET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setINN(charSequence.toString())
                }
            })

        compos.add(disposableINN)
    }

    private fun setRxToFCSET(fcs: TextInputEditText) {
        val disposableFCS: Disposable = RxTextView.textChanges(fcs)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setFCS(charSequence.toString())
                }
            })

        compos.add(disposableFCS)
    }

    private fun setRxToPhoneET(phone: TextInputEditText) {
        val disposablePhone: Disposable = RxTextView.textChanges(phone)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setPhone(charSequence.toString())
                }
            })

        compos.add(disposablePhone)
    }

    private fun setListenerSendDataBtn(sendDataBTN: MaterialButton) {
        sendDataBTN.setOnClickListener {


            viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)

        }
    }




    private fun setListenerExit(exit: View) {
        exit.setOnClickListener {
            Toast.makeText(requireActivity(), "exit is checked", Toast.LENGTH_LONG).show()
        }
    }


    private fun setListenerPrivacyPolicy(policy: View){
        policy.setOnClickListener {
            Toast.makeText(requireActivity(), "privacy policy is checked", Toast.LENGTH_LONG).show()
            viewModel.openPagePrivacyPolicy()
        }
    }

    private fun setListenerAgreeCB(agreeCB: CheckBox) {
        agreeCB.setOnClickListener {
            Toast.makeText(requireActivity(), "Organization is checked", Toast.LENGTH_LONG).show()
            viewModel.setAgree()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        println("------EnterRoleDataFragment onDestroyView()")
        compos.clear()
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
            EnterOrganizationDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}