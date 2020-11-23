package org.vernality.profitclub.view.organization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_members_requests.view.*
import kotlinx.android.synthetic.main.item_list_stocks.view.*
import kotlinx.android.synthetic.main.item_list_supplies.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.utils.Utils
import org.vernality.profitclub.utils.ui.setImageToImageView

public class SuppliesListRVAdapter(
    private val listener: OnListItemClickListener
) : RecyclerView.Adapter<SuppliesListRVAdapter.RecyclerItemViewHolder>() {

    private var data: List<CommercialOffer> = arrayListOf()

    fun setData(data: List<CommercialOffer>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_supplies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(commercialOffer: CommercialOffer) {
            itemView.tv_message_offer.setText(commercialOffer.message)
            val imageFile = commercialOffer.imageFile
            if(imageFile != null){
                itemView.iv_offerImage.setImageToImageView(imageFile.data)
            }

            itemView.tv_nameSupplier.setText(commercialOffer.contact)
            itemView.tv_period_offer.setText(Utils.getDataOfMyFormat(commercialOffer.createdAt))

            itemView.setOnClickListener { openOffer(commercialOffer) }
        }

    }

    private fun openOffer(commercialOffer: CommercialOffer) {
        listener.onItemClick(commercialOffer)
    }

    interface OnListItemClickListener {
        fun onItemClick(commercialOffer: CommercialOffer)
    }

}
