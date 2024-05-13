package com.example.taskqueue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskqueue.TaskListModel.TaskListModel

class TasksAdapter(private var tasks: List<TaskListModel>, context: Context):
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {
    class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int){
        val task = tasks[position]
        holder.titleTextView.text=task.name
        holder.dateTextView.text=task.date
        holder.timeTextView.text=task.time

    }
    fun refreshData(newTasks : List<TaskListModel>){
        tasks=newTasks
        notifyDataSetChanged()
    }

}