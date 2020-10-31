package org.vernality.profitclub.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_processing_list_recyclerview.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Organization

public class RequestRVAdapter(
    var list: ArrayList<Organization>
) :
    RecyclerView.Adapter<RequestRVAdapter.RecyclerItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_processing_list_recyclerview, parent, false)
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
            setStatusColor(organization)
            itemView.tv_role.text = organization.role
            itemView.tv_name_of_organization.text = organization.name
            itemView.tv_INN_value.text = organization.INN
            itemView.tv_contact_person_value.text = organization.FCS
            itemView.tv_phone_value.text = organization.phone
            itemView.tv_status.text = organization.state.toString()

        }

        private fun setStatusColor(organization: Organization) = when (organization.state) {
            0 -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestUnderConsideration))

            }
            1 -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestApproved))

            }
            2 -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestRejected))

            }
            else -> {
                itemView.tv_status.setTextColor(itemView.resources.getColor(R.color.colorRequestApproved))

            }
        }
    }

}
