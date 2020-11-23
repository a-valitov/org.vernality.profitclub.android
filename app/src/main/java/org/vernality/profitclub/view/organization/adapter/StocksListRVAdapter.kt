package org.vernality.profitclub.view.organization.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_stocks.view.*
import org.vernality.profitclub.R
import org.vernality.profitclub.model.data.Action
import org.vernality.profitclub.utils.ui.setImageToImageView


public class StocksListRVAdapter(
    private val listener: OnListItemClickListener
) : RecyclerView.Adapter<StocksListRVAdapter.RecyclerItemViewHolder>() {

    private var data: List<Action> = arrayListOf()

    fun setData(data: List<Action>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_stocks, parent, false)
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

        @SuppressLint("SetTextI18n")
        fun bind(action: Action) {
            itemView.tv_title_stocks.setText(action.message)
            itemView.tv_period_stocks.setText(action.startDate +"-"+action.endDate)
            itemView.tv_description_stocks.setText(action.descriptionOf)
            itemView.tv_linkn_stocks.setText(action.link)

            val imageFile = action.imageFile
            if(imageFile != null){
                itemView.iv_stocksImage.setImageToImageView(imageFile.data)
            }

            itemView.setOnClickListener {openAction(action)}
        }
    }

    private fun openAction(action: Action) {
        listener.onItemClick(action)
    }


    interface OnListItemClickListener {
        fun onItemClick(action: Action)
    }

}
