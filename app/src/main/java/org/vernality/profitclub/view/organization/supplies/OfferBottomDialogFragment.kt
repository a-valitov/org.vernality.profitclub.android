package org.vernality.profitclub.view.organization.supplies

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.parse.ParseFile
import kotlinx.android.synthetic.main.action_bottom_dialog_fragment.layout_offers_buttons
import kotlinx.android.synthetic.main.supply_bottom_dialog_fragment.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.utils.Utils
import org.vernality.profitclub.utils.ui.openFileInExternalApp
import org.vernality.profitclub.utils.ui.setImageToImageView
import org.vernality.profitclub.view.organization.adapter.OffersDocListRVAdapter
import java.io.File


class OfferBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var offerIV: ImageView
    private lateinit var offerCloseIV: ShapeableImageView
    private lateinit var contactNameTV: MaterialTextView
    private lateinit var date: MaterialTextView
    private lateinit var offersMessageTV: MaterialTextView
    private lateinit var acceptOfferBTN: MaterialButton
    private lateinit var rejectOfferBTN: MaterialButton
    private lateinit var layoutButtons: LinearLayout
    private var onApproveClickListener:OnOfferClickListener? = null
    private var onRejectClickListener:OnOfferClickListener? = null

    private val adapter: OffersDocListRVAdapter by lazy { OffersDocListRVAdapter(onListItemClickListener) }
    private lateinit var rv: RecyclerView

    private lateinit var offer: CommercialOffer


    private val onListItemClickListener: OffersDocListRVAdapter.OnListItemClickListener =
        object : OffersDocListRVAdapter.OnListItemClickListener {
            override fun onItemClick(numberFileInList: Int, format:String) {
                Toast.makeText(requireActivity()," number = $numberFileInList", Toast.LENGTH_SHORT).show()
                println("------parse file")
                val list = offer.getList<ParseFile>("attachmentFiles")
                val file = list?.get(numberFileInList)?.file

                openFileInExternalApp(file, format, requireContext())

            }
        }


    private val onApproveButtonClickListener =
        View.OnClickListener {
            onApproveClickListener?.onClick(offer)
//            dismiss()
        }

    private val onRejectButtonClickListener =
        View.OnClickListener {
            onRejectClickListener?.onClick(offer)
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

        return inflater.inflate(R.layout.supply_bottom_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offerIV = iv_offers_image
        offerCloseIV = iv_offer_close
        contactNameTV = tv_offers_contact_name
        date = tv_offers_date
        offersMessageTV = tv_offers_message
        acceptOfferBTN = btn_approve_offer
        rejectOfferBTN = btn_reject_offer
        layoutButtons = layout_offers_buttons
        rv = rv_offers_doc
        rv.adapter = adapter

        adapter.setData(offer.getList("attachmentNames"))

        offerCloseIV.setOnClickListener { dismiss() }
        acceptOfferBTN.setOnClickListener(onApproveButtonClickListener)
        rejectOfferBTN.setOnClickListener(onRejectButtonClickListener)


        initViews()

    }

    fun initViews(){
        val imageFile = offer.imageFile
        if(imageFile != null){
            offerIV.setImageToImageView(imageFile.data)
        }

        contactNameTV.setText((offer.supplier as Supplier).fetchIfNeeded<Supplier>().contactName)
        offersMessageTV.setText(offer.message)
        date.setText(Utils.getDataOfMyFormat(offer.createdAt))

    }

    fun setApproveClickListener(listener: OnOfferClickListener){
        onApproveClickListener = listener
    }

    fun setRejectClickListener(listener: OnOfferClickListener){
        onRejectClickListener = listener
    }


    override fun onDestroyView() {
        onApproveClickListener = null
        super.onDestroyView()
    }


    interface OnOfferClickListener {
        fun onClick(offer: CommercialOffer)
    }




    companion object {
        fun newInstance(_offer: CommercialOffer): OfferBottomDialogFragment {
            val actionBottomDialogFragment = OfferBottomDialogFragment()
            actionBottomDialogFragment.offer = _offer
            return actionBottomDialogFragment
        }
    }
}
