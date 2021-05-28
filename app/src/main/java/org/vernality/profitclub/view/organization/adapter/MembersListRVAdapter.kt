package org.vernality.profitclub.view.organization.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_members.view.*
import kotlinx.android.synthetic.main.item_list_stocks.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.model.data.Member
import org.vernality.profitclub.model.data.Organization
import org.vernality.profitclub.utils.ui.setImageToImageView

public class MembersListRVAdapter(
    private val listener: OnListItemClickListener
) : RecyclerView.Adapter<MembersListRVAdapter.RecyclerItemViewHolder>() {

    private var data: List<Member> = arrayListOf()

    fun setData(data: List<Member>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_members, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        println("-----list.size ="+data.size)
        return data.size
    }

    inner class RecyclerItemViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(member: Member) {
            itemView.tv_members_name.setText(member.firstName+" "+member.lastName)
//            itemView.tv_members_date.setText(member.createdAt.toString())
            itemView.setOnClickListener { openMember(member) }

            val imageFile = member.imageFile
            if(imageFile != null){
                itemView.iv_membersImage.setImageToImageView(imageFile.data)
            }
        }


    }

    private fun openMember(member: Member) {
        listener.onItemClick(member)
    }

    interface OnListItemClickListener {
        fun onItemClick(member: Member)
    }

}
