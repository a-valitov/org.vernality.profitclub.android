package org.vernality.profitclub.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_enter_role.view.*
import kotlinx.android.synthetic.main.item_enter_role.view.*
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.view.activities.EnterRoleActivity
import org.vernality.profitclub.view_model.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [RoleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterRoleDataFragment : Fragment() {

    val compos = CompositeDisposable()

    private val viewModel by viewModel<EnterRoleDataViewModel>()



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fields:MutableMap<FieldOrg, String?>

    lateinit var sendDataBtn:  MaterialButton
    lateinit var nameOfOrganizationET: TextInputEditText
    lateinit var INNOfOrganizationET: TextInputEditText
    lateinit var FCSET: TextInputEditText
    lateinit var phoneET: TextInputEditText
    lateinit var privacyPolicyTV: TextInputEditText
    lateinit var exit: ConstraintLayout
    lateinit var roleTV: TextView
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
        val root = inflater.inflate(R.layout.fragment_enter_role, container, false)

        init(root)

        initResultSendData()

        initMessageLiveData()

        if (savedInstanceState == null) {
            fields = viewModel.getFields()
            setFields(fields)
        } else {

        }

        return root
    }

    private fun setFields(fields: MutableMap<FieldOrg, String?>) {
        nameOfOrganizationET.setText(fields[FieldOrg.Name])
        INNOfOrganizationET.setText(fields[FieldOrg.INN])
        FCSET.setText(fields[FieldOrg.ContactName])
        phoneET.setText(fields[FieldOrg.Phone])
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun init(root:View){
        sendDataBtn = root.btn_send
        nameOfOrganizationET= root.et_enter_name_of_organization
        INNOfOrganizationET= root.et_enter_INN
        FCSET= root.et_enter_FCS
        phoneET = root.et_enter_phone
        privacyPolicyTV = root.et_enter_phone
        exit = root.exit_enter_role
        roleTV = root.tittle_role
        roleHideTV = root.tv_input_hint

        setRxToNameOfOrgET(nameOfOrganizationET)
        setRxToINNET(INNOfOrganizationET)
        setRxToFCSET(FCSET)
        setRxToPhoneET(phoneET)

        setListenerExit(exit)
        setListenerSendDataBtn(sendDataBtn)

        val activityViewModel = (requireActivity() as EnterRoleActivity).viewModelRoleActivity

        setObserverActivityLivedata(activityViewModel)


    }

    private fun setObserverActivityLivedata(activityViewModel: EnterRoleActivityViewModel) {
        activityViewModel.getRoleLiveData().observe(viewLifecycleOwner, Observer {

            when (it) {
                Role.Provider -> {
                    roleTV.setText(R.string.provider)
                    roleHideTV.setText(R.string.provider_description)
                    viewModel.setRole(Role.Provider)
                }
                Role.Organization -> {
                    roleTV.setText(R.string.organization)
                    roleHideTV.setText(R.string.organization_description)
                    viewModel.setRole(Role.Organization)
                }
                Role.Participant -> {
                    roleTV.setText(R.string.participant_of_the_organization)
                    roleHideTV.setText(R.string.participant_of_the_organization_description)
                    viewModel.setRole(Role.Participant)
                }
            }
        })
    }

    private fun showAlertDialog(){
        val alertResultDialog
                = AlertRoleDialogFragment.newInstance(View.OnClickListener {
            navig()

        })
        alertResultDialog.show(parentFragmentManager, "ggg")


    }

    fun navig(){

    }


    private fun initResultSendData(){
        viewModel.resultLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it == Result.Success) {
                Toast.makeText(requireActivity(), "Registration is success", Toast.LENGTH_LONG).show()
                showAlertDialog()
            }
            else {
                Toast.makeText(requireActivity(), "Registration is error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initMessageLiveData(){
        viewModel.messageLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if(it != null) {
                showMessage(it)
                viewModel.deleteShowedMessage()
            }
        })
    }

    fun showMessage(message: String){

        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
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
            viewModel.sendData()

        }
    }




    private fun setListenerExit(exit: View) {
        exit.setOnClickListener {
            Toast.makeText(requireActivity(), "exit is checked", Toast.LENGTH_LONG).show()
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
            EnterRoleDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}