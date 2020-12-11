package org.vernality.profitclub.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment_success.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.utils.ui.UIUtils
import org.vernality.profitclub.view_model.Role

enum class TypeDialogFragment{
    Registration, RoleApproveAdmin, LogOrgAccount, ResetPassword, RequestApprovedAdmin,
    ApproveAction, ApproveRequestIntoOrg, SubmitCommercialOffer, ApproveDelivery
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

    private var name: String? = null
    private var role: String? = null
    private var typeOrg: Role? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_success, container, false)

        init(root)

        setListenerOnAction(actionListener)

        initRole()

        initTitlesOnViews()



//        dialog?.window?.setDimAmount(1f)

        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return root
    }

    private fun init(root: View){
        mesageTV = root.tv_message
        subMesageTV = root.tv_subMessage
        actionTV = root.tv_action
        backTV = root.tv_back

        setListenerOnBack(backTV)
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

    fun setName(_name: String?){
        name = _name
    }

    fun <T>setRole(_role: T){
        when(_role){
            is Member -> typeOrg = Role.Member
            is Supplier -> typeOrg = Role.Supplier
            is Organization -> typeOrg = Role.Organization
        }
    }

    private fun initRole(){
        when(typeOrg){
            Role.Member -> role = resources.getString(R.string.of_members)
            Role.Supplier -> {role = requireActivity().getString(R.string.of_suppliers)
            println("----- role = "+ role)}
            Role.Organization -> role = resources.getString(R.string.of_organizations)
        }
    }

    private fun initTitlesOnViews(){
        UIUtils.configureDialogFragment(this, typeDialogFragment)
    }


    fun setTitleOnViews(array: Array<String>){
        println("------setTitleOnViews")
        mesageTV.setText(array[0])
        subMesageTV.setText(array[1].replace("name", name?:"поставщика")
            .replace("role", role?:"") )
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