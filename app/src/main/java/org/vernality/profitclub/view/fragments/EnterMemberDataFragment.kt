package org.vernality.profitclub.view.fragments

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
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_enter_organization.view.*
import kotlinx.android.synthetic.main.item_enter_organization.view.*
import org.koin.android.ext.android.get
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.AppState
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.view_model.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val MEMBER_FIRST_NAME = "memberFirstName"
val MEMBER_LAST_NAME = "memberLastName"

/**
 * A simple [Fragment] subclass.
 * Use the [RoleSelectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterMemberDataFragment : Fragment() {


    val compos = CompositeDisposable()

    private val viewModel = get<EnterMemberDataFragmentViewModel>()


    private val observer = Observer<AppState> { renderData(it) }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var member:Member

    lateinit var sendDataBtn:  MaterialButton
    lateinit var firstNameET: TextInputEditText
    lateinit var lastNameET: TextInputEditText
    lateinit var privacyPolicyTV: TextView
    lateinit var exit: ConstraintLayout
    lateinit var roleTV: TextView
    lateinit var agreeCB: CheckBox
    lateinit var roleHideTV: TextView
    private lateinit var loadingLayout: FrameLayout



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
        val root = inflater.inflate(R.layout.fragment_enter_member, container, false)

        init(root)

//        initResultSendData()
//
//        initMessageLiveData()

        if (savedInstanceState == null) {
            member = viewModel.member
            setFields(member)
        } else {

        }

        return root
    }

    private fun setFields(member: Member) {
        firstNameET.setText(member.firstName)
        lastNameET.setText(member.lastName)
    }


    fun init(root:View){
        sendDataBtn = root.btn_send
        firstNameET= root.findViewById(R.id.et_enter_first_name)
        lastNameET= root.findViewById(R.id.et_enter_last_name)
        privacyPolicyTV = root.findViewById(R.id.tv_privacy_policy)
        exit = root.exit_enter_role
        roleTV = root.tittle_role
        agreeCB = root.cb_agree
        roleHideTV = root.tv_input_hint
        loadingLayout = root.loading_frame_layout


        setRxToNameOfOrgET(firstNameET)
        setRxToINNET(lastNameET)

        setListenerExit(exit)
        setListenerSendDataBtn(sendDataBtn)
        setListenerPrivacyPolicy(privacyPolicyTV)
        setListenerAgreeCB(agreeCB)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                navigateTo()
                loadingLayout.visibility = View.GONE
            }
            is AppState.Loading -> {
                Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                Toast.makeText(requireActivity(), "Error \n ${appState.error}", Toast.LENGTH_LONG).show()
                loadingLayout.visibility = View.GONE
            }
        }
    }

//    private fun showAlertDialog(){
//        val alertResultDialog
//                = AlertRoleDialogFragment.newInstance(View.OnClickListener {
//            navigateTo()
//
//        })
//        alertResultDialog.show(parentFragmentManager, "ggg")
//
//
//    }

    fun navigateTo(){

        findNavController().navigate(R.id.action_enterMemberFragment_to_selectOrgForMemberFragment)
    }


//    private fun initResultSendData(){
//        viewModel.resultLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
//            if(it == Result.Success) {
//                Toast.makeText(requireActivity(), "Registration is success", Toast.LENGTH_LONG).show()
//                navigateTo()
//            }
//            else {
//                Toast.makeText(requireActivity(), "Registration is error", Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    private fun initMessageLiveData(){
//        viewModel.messageLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
//            if(it != null) {
//                showMessage(it)
//                viewModel.deleteShowedMessage()
//            }
//        })
//    }

//    fun showMessage(message: String){
//
//        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
//    }








    private fun setRxToNameOfOrgET(nameOfOrganizationET: TextInputEditText) {
        val disposableName: Disposable = RxTextView.textChanges(nameOfOrganizationET)
            .subscribe(object : Consumer<CharSequence?> {
                @Throws(Exception::class)
                override fun accept(charSequence: CharSequence?) {
                    //Add your logic to work on the Charsequence
                    if(charSequence!!.isEmpty())Toast.makeText(requireActivity(), "password 2 enter", Toast.LENGTH_LONG).show()
                    viewModel.setFirstName(charSequence.toString())
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
                    viewModel.setLastName(charSequence.toString())
                }
            })

        compos.add(disposableINN)
    }



    private fun setListenerSendDataBtn(sendDataBTN: MaterialButton) {
        sendDataBTN.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(MEMBER_FIRST_NAME, firstNameET.text.toString())
            bundle.putString(MEMBER_LAST_NAME, lastNameET.text.toString())

            findNavController().navigate(R.id.action_enterMemberFragment_to_selectOrgForMemberFragment, bundle)

//            viewModel.getLiveDataAndStartGetResult().observe(requireActivity(), observer)

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
            EnterMemberDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}