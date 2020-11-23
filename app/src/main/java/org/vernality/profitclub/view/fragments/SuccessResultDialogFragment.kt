package org.vernality.profitclub.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_success.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.utils.ui.UIUtils

enum class TypeDialogFragment{
    Registration, RoleApproveAdmin, LogOrgAccount, ResetPassword, RequestApprovedAdmin,
    ApprovePromotion, ApproveRequestIntoOrg, SubmitCommercialOffer, ApproveDelivery
}


class SuccessResultDialogFragment(private var typeDialogFragment: TypeDialogFragment,
private var actionListener: ()->Unit) : SupportBlurDialogFragment() {

    companion object {
        fun newInstance(typeDialogFragment: TypeDialogFragment, actionListener: ()->Unit): SuccessResultDialogFragment {
            return SuccessResultDialogFragment(typeDialogFragment, actionListener)
        }
    }

    private lateinit var mBlurEngine: BlurDialogEngine
    private lateinit var mesageTV: TextView
    private lateinit var subMesageTV: TextView
    private lateinit var actionTV: TextView
    lateinit var backTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_success, container, false)
//        root.tv_resume.setOnClickListener {
//            Toast.makeText(requireActivity(), "TV resume clicked ", Toast.LENGTH_LONG).show()
//            //findNavController().navigate(R.id.action_registrationFragment_to_roleFragment)
//            val intent = Intent(requireActivity(), EnterRoleActivity::class.java)
//            requireActivity().startActivity(intent)
//            dismiss()
//            requireActivity().finish()
//        }

        init(root)

        setListenerOnAction(actionListener)

        initTitlesOnViews()

        dialog?.window?.setDimAmount(1f)

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return root
    }

    private fun init(root: View){
        mesageTV = root.tv_message
        subMesageTV = root.tv_subMessage
        actionTV = root.tv_action
        backTV = root.tv_back
    }

    fun setListenerOnAction(func: ()->Unit){
        actionTV.setOnClickListener {
            func()
            dismiss()
        }
    }

    fun setListenerOnBack(view: View){
        view.setOnClickListener { dismiss() }
    }

    fun setName(name: String){
        val message = mesageTV.text
        val subMessage = subMesageTV.text
        mesageTV.setText(message.toString().replace("name", name, true))
        subMesageTV.setText(subMessage.toString().replace("name", name, true))
    }

    private fun initTitlesOnViews(){
        UIUtils.configureDialogFragment(this, typeDialogFragment)
    }


    fun setTitleOnViews(array: Array<String>){
        mesageTV.setText(array[0])
        subMesageTV.setText(array[1])
        actionTV.setText(array[2])
        backTV.setText(array[3])
    }



    override fun getBlurRadius(): Int {
        return 25
    }

    override fun isRenderScriptEnable(): Boolean {
        return true
    }


    override fun isDimmingEnable(): Boolean {
        return false
    }

    override fun isActionBarBlurred(): Boolean {
        return false
    }

    override fun isDebugEnable(): Boolean {
        return false
    }
}