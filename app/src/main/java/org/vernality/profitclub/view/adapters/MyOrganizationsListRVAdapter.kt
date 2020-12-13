package org.vernality.profitclub.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_processing_list_recyclerview.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.CommercialOffer
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.view.organization.adapter.SuppliesListRVAdapter

public class MyOrganizationsListRVAdapter(
    private val listener: OnListItemClickListener
) :
    RecyclerView.Adapter<MyOrganizationsListRVAdapter.RecyclerItemViewHolder>() {

    private var data: List<Organization> = arrayListOf()

    fun setData(data: List<Organization>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_processing_list_recyclerview, parent, false)
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

        fun bind(organization: Organization) {
            setStatusColor(organization)
            itemView.tv_name_of_organization.text = organization.name
            itemView.tv_INN_value.text = organization.inn
            itemView.tv_contact_person_value.text = organization.contactName
            itemView.tv_phone_value.text = organization.phone
            itemView.tv_status.text = organization.statusString
            itemView.setOnClickListener { openOffer(organization) }

        }

        private fun setStatusColor(organization: Organization) = when (organization.statusString) {
            Organization.OrganizationStatus.onReview.name -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestUnderConsideration))

            }
            Organization.OrganizationStatus.approved.name -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestApproved))

            }
            Organization.OrganizationStatus.rejected.name -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestRejected))

            }
            Organization.OrganizationStatus.excluded.name -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestExcluded))

            }
            else -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestApproved))

            }
        }
    }

    private fun openOffer(organization: Organization) {
        listener.onItemClick(organization)
    }

    interface OnListItemClickListener {
        fun onItemClick(organization: Organization)
    }

}
