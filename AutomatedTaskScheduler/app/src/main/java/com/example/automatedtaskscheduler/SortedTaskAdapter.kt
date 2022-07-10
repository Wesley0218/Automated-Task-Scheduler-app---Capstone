package com.example.automatedtaskscheduler

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.automatedtaskscheduler.fragments.EditFragment
import java.text.SimpleDateFormat



class SortedTaskAdapter: RecyclerView.Adapter<SortedTaskAdapter.SortedTaskViewHolder>() {
    private var taskList: ArrayList<SortedTaskList> = ArrayList()
    private lateinit var fragment: EditFragment
    private lateinit var mListener: onItemClickListener
    private var onClickRemoveItem: ((SortedTaskList) -> Unit)? = null
    private var onClickComplete: ((SortedTaskList) -> Unit)? = null


    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnClickRemoveItem(callback: (SortedTaskList) -> Unit){
        this.onClickRemoveItem = callback
    }

    fun setOnClickComplete(callback: (SortedTaskList) -> Unit){
        this.onClickComplete = callback
    }


    fun addItems(items: ArrayList<SortedTaskList>){
        this.taskList = items

        notifyDataSetChanged()
        println("test button")
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SortedTaskViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_task, parent, false )
    ,mListener)

    override fun onBindViewHolder(holder: SortedTaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bindView(task)
        holder.btnComplete.setOnClickListener { onClickComplete?.invoke(task)  }
        holder.btnRemove.setOnClickListener { onClickRemoveItem?.invoke(task) }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class SortedTaskViewHolder(var view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        private var taskName = view.findViewById<TextView>(R.id.taskName)
        private var urgency = view.findViewById<TextView>(R.id.URGENCY)
        private var category = view.findViewById<TextView>(R.id.CATEGORY)
        var btnComplete = view.findViewById<Button>(R.id.complete)
        var btnRemove = view.findViewById<Button>(R.id.remove)

        val hourFormat = SimpleDateFormat("HH")
        val minFormat = SimpleDateFormat("mm")
        val timeFormat = SimpleDateFormat("HH:mm aa")
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val ampmFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aa")
        val datetimeFormat = SimpleDateFormat("MM/dd/yyyy HH:mm")

        init{

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

        fun bindView(task: SortedTaskList){
            taskName.text = "Task Name:" + task.TaskName
            urgency.text = "Time: " + ampmFormat.format(datetimeFormat.parse(task.sTime)) + " - " + ampmFormat.format(datetimeFormat.parse(task.eTime))
            category.text = "Task Category: " + task.Category
        }

    }
}