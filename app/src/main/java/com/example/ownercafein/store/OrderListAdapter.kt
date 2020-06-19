package com.example.ownercafein.store

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ownercafein.Owners
import com.example.ownercafein.R
import com.example.ownercafein.databinding.ItemOrderlistBinding

class OrderListAdapter(
    private val context: Context,
    private val items: List<OrderList>,
    private val clickListener: (orderlist: OrderList) -> Unit) :
    RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {
    class OrderListViewHolder(val binding: ItemOrderlistBinding): RecyclerView.ViewHolder(binding.root){
        var statusBtn: Button? = null

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orderlist, parent, false)
        val viewHolder = OrderListViewHolder(ItemOrderlistBinding.bind(view))
        val itemView =  viewHolder.itemView.findViewById<Button>(R.id.orderCheckButton)
        view.setOnClickListener{
            clickListener.invoke(items[viewHolder.adapterPosition])
            itemView.text = "준비완료"
            var intent = Intent()
            intent.setClassName("com.example.cafein", "com.example.cafein.MainActivity")
            intent.putExtra("go", "주문이 들어왔습니다.")
            context.startActivity(intent)
        }

        itemView.setOnClickListener {
            clickListener.invoke(items[viewHolder.adapterPosition])
            itemView.text = "준비완료"
            var intent = Intent()
            intent.setClassName("com.example.cafein", "com.example.cafein.MainActivity")
            intent.putExtra("go", "주문이 들어왔습니다.")
            context.startActivity(intent)
        }


        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.binding.orderlist = items[position]
    }


}