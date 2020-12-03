package org.vernality.profitclub.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_org_for_member_recyclerview.view.*
import kotlinx.android.synthetic.main.item_processing_list_recyclerview.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Organization

public class ListOrgForMemberRVAdapter(
    private var onListItemClickListener: OnListItemClickListener, var list: List<Organization>
) : RecyclerView.Adapter<ListOrgForMemberRVAdapter.RecyclerItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_org_for_member_recyclerview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class RecyclerItemViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(organization: Organization) {
            itemView.tv_name_of_organization_member.text = organization.name
            itemView.tv_contact_person_value_member.text = organization.contactName
            itemView.tv_phone_value_member.text = organization.phone
            itemView.setOnClickListener { openInNewWindow(organization) }
        }


    }

    private fun openInNewWindow(organization: Organization) {
        onListItemClickListener.onItemClick(organization)
    }

    interface OnListItemClickListener {
        fun onItemClick(organization: Organization)
    }

}
