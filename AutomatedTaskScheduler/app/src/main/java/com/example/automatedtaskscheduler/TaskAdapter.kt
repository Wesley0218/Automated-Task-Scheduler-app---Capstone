package com.example.automatedtaskscheduler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var taskList: ArrayList<TaskList> = ArrayList()

    fun addItems(items: ArrayList<TaskList>){
        this.taskList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_task, parent, false )
    )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bindView(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var taskName = view.findViewById<TextView>(R.id.taskName)
        private var urgency = view.findViewById<TextView>(R.id.URGENCY)
        private var category = view.findViewById<TextView>(R.id.CATEGORY)

        fun bindView(task: TaskList){
            taskName.text = task.TaskName
            urgency.text = task.Urgency
            category.text = task.Category
        }
    }
}