package org.vernality.profitclub.view.organization.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.action_bottom_dialog_fragment.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.StatePeriod
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.utils.Utils
import org.vernality.profitclub.utils.ui.setImageToImageView


class ActionBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var actionsIV: ImageView
    private lateinit var actionCloseIV: ShapeableImageView
    private lateinit var contactNameTV: MaterialTextView
    private lateinit var link: LinearLayout
    private lateinit var linkTV: MaterialTextView
    private lateinit var actionsMessageTV: MaterialTextView
    private lateinit var actionsDescriptionTV: MaterialTextView
    private lateinit var actionsPeriodTV: MaterialTextView
    private lateinit var acceptActionBTN: MaterialButton
    private lateinit var rejectActionBTN: MaterialButton
    private lateinit var layoutButtons: LinearLayout
    private var buttonsLayoutIsInvisible = false
    private var onAcceptClickListener:OnClickListener? = null
    private var onRejectClickListener:OnClickListener? = null
    private var onLinkClickListener:OnLinkClickListener? = null

    private lateinit var action: Action

    private var showButtons = true

    private val onAcceptButtonClickListener =
        View.OnClickListener {
            onAcceptClickListener?.onClick(action)
//            dismiss()
        }

    private val onRejectButtonClickListener =
        View.OnClickListener {
            onRejectClickListener?.onClick(action)
//            dismiss()
        }

    private val onLinkButtonClickListener =
        View.OnClickListener {
            onLinkClickListener?.onClick(action.link)
//            dismiss()
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dialog!!.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(
                    com.google.android.material.R.id.design_bottom_sheet
                )
            BottomSheetBehavior.from<View?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        return inflater.inflate(R.layout.action_bottom_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionsIV = iv_actions_image
        actionCloseIV = iv_action_close
        contactNameTV = tv_actions_contact_name
        link = layout_actions_link
        linkTV = tv_actions_link
        actionsMessageTV = tv_actions_message
        actionsDescriptionTV = tv_actions_description
        actionsPeriodTV = tv_actions_period
        acceptActionBTN = btn_accept_action
        rejectActionBTN = btn_reject_action
        layoutButtons = layout_offers_buttons

        actionCloseIV.setOnClickListener { dismiss() }
        acceptActionBTN.setOnClickListener(onAcceptButtonClickListener)
        rejectActionBTN.setOnClickListener(onRejectButtonClickListener)
        link.setOnClickListener(onLinkButtonClickListener)

        if(action.statePeriod == StatePeriod.Past || buttonsLayoutIsInvisible)
        {
            linkTV.setText(R.string.actions_has_passed)
            layoutButtons.visibility = View.GONE
        }

        initViews()

    }

    fun disableButtonsLayout(){
        buttonsLayoutIsInvisible = true
    }

    fun initViews(){
        val imageFile = action.imageFile
        if(imageFile != null){
            actionsIV.setImageToImageView(imageFile.data)
        }


        contactNameTV.setText((action.supplier as Supplier).contactName)
        actionsMessageTV.setText(action.message)
        actionsDescriptionTV.setText(action.descriptionOf)
        actionsPeriodTV.setText(Utils.getDataOfMyFormat(action.startDate!!)+
                "-"+Utils.getDataOfMyFormat(action.endDate!!))

    }

    fun setAcceptClickListener(listener: OnClickListener){
        onAcceptClickListener = listener
    }

    fun setRejectClickListener(listener: OnClickListener){
        onRejectClickListener = listener
    }

    fun setLinkClickListener(listener: OnLinkClickListener){
        onLinkClickListener = listener
    }

    override fun onDestroyView() {
        onAcceptClickListener = null
        super.onDestroyView()
    }


    interface OnClickListener {
        fun onClick(action: Action)
    }

    interface OnLinkClickListener {
        fun onClick(link: String?)
    }



    companion object {
        fun newInstance(_action: Action): ActionBottomDialogFragment {
            val actionBottomDialogFragment = ActionBottomDialogFragment()
            actionBottomDialogFragment.action = _action
            return actionBottomDialogFragment
        }
    }
}
