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

    inner class UserView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_tite)
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserView {
        val view: View = LayoutInflater.from(context).inflate(R.layout.itemtodo, parent, false)
        return UserView(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: UserView, position: Int) {
        // Menampilkan "users" di TextView titleTextView
        holder.titleTextView.text = arrayList[position].users

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(arrayList[position]) // Null safety
        }
    }

    fun setOnItemClickListenerManually(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(user: User?)
    }
}