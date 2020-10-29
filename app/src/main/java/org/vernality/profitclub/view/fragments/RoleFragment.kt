package org.vernality.profitclub.view.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.vansuita.gaussianblur.GaussianBlur
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.android.synthetic.main.fragment_role.view.*
import kotlinx.android.synthetic.main.item_select_role.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.vernality.profitclub.R
import org.vernality.profitclub.view_model.RegistrationViewModel
import org.vernality.profitclub.view_model.Role
import org.vernality.profitclub.view_model.RoleSelectViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoleFragment : Fragment() {

    private val viewModel by viewModel<RoleSelectViewModel>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    lateinit var radioGroup: RadioGroup
    lateinit var providerCB: CheckBox
    lateinit var organizationCB: CheckBox
    lateinit var participantCB: CheckBox

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
        val root = inflater.inflate(R.layout.fragment_role, container, false)


        init(root)

        initCheckBox(viewModel.getRole())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun init(root:View){
        val exit = root.exit
        providerCB = root.checkBox_provider
        organizationCB = root.checkBox_organization
        participantCB = root.checkBox_participant
        val resumeBTN = root.btn_resume

        setListenerExit(exit)
        setListenerProvider(providerCB)
        setListenerOrganization(organizationCB)
        setListenerParticipant(participantCB)
        setListenerResumeBtn(resumeBTN)


    }

    private fun setListenerResumeBtn(resumeBTN: MaterialButton) {
        resumeBTN.setOnClickListener {
            Toast.makeText(requireActivity(), "button resume is checked", Toast.LENGTH_LONG).show()
            viewModel.resume()
        }
    }

    private fun setListenerParticipant(participantCB: CheckBox) {
        participantCB.setOnClickListener {
            Toast.makeText(requireActivity(), "Participant is checked", Toast.LENGTH_LONG).show()
            viewModel.setRoleParticipant()
            setCheckBoxGroup(participantCB)
        }

    }

    private fun setListenerOrganization(organizationCB: CheckBox) {
        organizationCB.setOnClickListener {
            Toast.makeText(requireActivity(), "Organization is checked", Toast.LENGTH_LONG).show()
            viewModel.setRoleOrganization()
            setCheckBoxGroup(organizationCB)
        }

    }

    private fun setListenerProvider(providerCB: CheckBox) {
        providerCB.setOnClickListener {
            Toast.makeText(requireActivity(), "Provider is checked", Toast.LENGTH_LONG).show()
            viewModel.setRoleProvider()
            setCheckBoxGroup(providerCB)
        }

    }

    private fun setListenerExit(exit: View) {
        exit.setOnClickListener {
            Toast.makeText(requireActivity(), "exit is checked", Toast.LENGTH_LONG).show()
        }
    }

    fun setCheckBoxGroup(checkBox: CheckBox){
        when(checkBox.id){
            providerCB.id -> {
                if(organizationCB.isChecked) organizationCB.toggle()
                if(participantCB.isChecked) participantCB.toggle()
                if(providerCB.isChecked) else providerCB.toggle()}
            participantCB.id -> {
                if(organizationCB.isChecked) organizationCB.toggle()
                if(providerCB.isChecked) providerCB.toggle()
                if(participantCB.isChecked) else participantCB.toggle()}
            organizationCB.id -> {
                if(participantCB.isChecked) participantCB.toggle()
                if(providerCB.isChecked) providerCB.toggle()
                if(organizationCB.isChecked) else organizationCB.toggle()}
        }
    }

    fun initCheckBox(role: Role?){
        when(role){
            Role.Provider -> setCheckBoxGroup(providerCB)
            Role.Organization -> setCheckBoxGroup(organizationCB)
            Role.Participant -> setCheckBoxGroup(participantCB)
        }
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
            RoleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}