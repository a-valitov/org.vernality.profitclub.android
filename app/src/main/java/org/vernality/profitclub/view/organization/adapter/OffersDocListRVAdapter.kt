package org.vernality.profitclub.view.organization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_members_requests.view.*
import kotlinx.android.synthetic.main.item_list_offers_doc.view.*
import kotlinx.android.synthetic.main.item_list_stocks.view.*
import kotlinx.android.synthetic.main.item_list_supplies.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.model.data.Supplier
import org.vernality.profitclub.utils.Utils
import org.vernality.profitclub.utils.ui.setImageToImageView

public class OffersDocListRVAdapter(
    private val listener: OnListItemClickListener
) : RecyclerView.Adapter<OffersDocListRVAdapter.RecyclerItemViewHolder>() {

    private var data:  MutableList<String> = mutableListOf()

    fun setData(list: MutableList<String>?) {
        println("---------setData")
        list?.let { data = it }

        list?.forEach { println("-----"+it) }
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_offers_doc, parent, false)
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

        fun bind(string: String) {
            val format = string.substringAfter('.')
            itemView.tv_format_offers_doc.setText(format)
            itemView.tv_name_offers_doc.setText(string)
            itemView.setOnClickListener{openOffer(layoutPosition, format)}

        }

    }

    private fun openOffer(int: Int, format: String) {
        listener.onItemClick(int, format)
    }

    interface OnListItemClickListener {
        fun onItemClick(int: Int, format: String)
    }

}
