package com.luizeduardobrandao.apptarefas.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.apptarefas.databinding.ItemTaskListBinding
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.view.viewholder.TaskViewHolder


class TaskAdapter: RecyclerView.Adapter<TaskViewHolder>() {

    private var listTasks: List<TaskModel> = arrayListOf()
    private lateinit var listener: TaskListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemTaskListBinding.inflate(inflater, parent, false)
        return TaskViewHolder(itemBinding, listener)
    }

    override fun getItemCount(): Int {
        return listTasks.count()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    fun updateTasks(list: List<TaskModel>){
        listTasks = list
        notifyDataSetChanged()
    }

    fun attachListener(taskListener: TaskListener){
        listener = taskListener
    }
}