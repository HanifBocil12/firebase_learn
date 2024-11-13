package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class TodoAdapter(
    var todos: List<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoView>() {

    inner class TodoView(itemview: View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemtodo, parent, false)
        return TodoView(view)
    }

    override fun onBindViewHolder(holder: TodoView, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_tite).text = todos[position].title
//            findViewById<Button>(R.id.btn_to).setOnClickListener()
//            {
//                val targetClass = Class.forName("com.example.individuproject.${todos[position].title}")
//                val intent = Intent(context, targetClass)
//                context.startActivity(intent)
//            }
        }
    }

    override fun getItemCount(): Int {
        return  todos.size
    }
}