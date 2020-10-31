package org.vernality.profitclub.view.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.vansuita.gaussianblur.GaussianBlur
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.android.synthetic.main.fragment_enter_role.view.*
import kotlinx.android.synthetic.main.fragment_role.view.*
import kotlinx.android.synthetic.main.item_enter_role.view.*
import kotlinx.android.synthetic.main.item_select_role.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
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

    lateinit var sendDataBtn:  MaterialButton
    lateinit var nameOfOrganizationET: TextInputEditText
    lateinit var INNOfOrganizationET: TextInputEditText
    lateinit var FCSET: TextInputEditText
    lateinit var phoneET: TextInputEditText
    lateinit var privacyPolicyTV: TextInputEditText
    lateinit var exit: ConstraintLayout


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

        println("-------viewModel = " +viewModel)

//        setFields(viewModel.getFields())


        return root
    }

    private fun setFields(fields: MutableMap<RoleField, String?>) {
        for (a in fields){

            println("-----" + a.value + "   "+ a.key)
        }
        nameOfOrganizationET.setText(fields[RoleField.Name])
        INNOfOrganizationET.setText(fields[RoleField.INN])
        FCSET.setText(fields[RoleField.FCS])
        phoneET.setText(fields[RoleField.Phone])
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

        setRxToNameOfOrgET(nameOfOrganizationET)
        setRxToINNET(INNOfOrganizationET)
        setRxToFCSET(FCSET)
        setRxToPhoneET(phoneET)

        setListenerExit(exit)
        setListenerSendDataBtn(sendDataBtn)


    }

    private fun showAlertDialog(){
        val alertResultDialog
                = AlertRoleDialogFragment.newInstance(View.OnClickListener {
            navig()

        })
        alertResultDialog.show(parentFragmentManager, "ggg")


    }

    fun navig(){

        //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
//        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
//        Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
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
                    viewModel.setFCS(charSequence.toString())
                }
            })

        compos.add(disposablePhone)
    }

    private fun setListenerSendDataBtn(sendDataBTN: MaterialButton) {
        sendDataBTN.setOnClickListener {
            Toast.makeText(requireActivity(), "button resume is checked", Toast.LENGTH_LONG).show()
            viewModel.sendData()
            findNavController().navigate(R.id.action_enterRoleFragment_to_BlankFragment)
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