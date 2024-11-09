package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    var context: Context,
    var arrayList: List<User>
) : RecyclerView.Adapter<UserAdapter.UserView>() {
    var onItemClickListener: OnItemClickListener? = null

    inner class UserView(itemview: View) : RecyclerView.ViewHolder(itemview){
        val titleTextView: TextView = itemView.findViewById(R.id.tv_title)
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserView {
       val view : View = LayoutInflater.from(context).inflate(R.layout.itemtodo, parent, false)
        return UserView(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size;
    }

    override fun onBindViewHolder(holder: UserView, position: Int) {
        holder.titleTextView.setText(arrayList[position].title)
        holder.timeTextView.setText(arrayList[position].time)
        holder.itemView.setOnClickListener {
            onItemClickListener!!.onClick(arrayList[position])
        }
    }

    fun setOnItemClickListenerManually(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    public interface OnItemClickListener {
        fun onClick(user: User?)
    }
}