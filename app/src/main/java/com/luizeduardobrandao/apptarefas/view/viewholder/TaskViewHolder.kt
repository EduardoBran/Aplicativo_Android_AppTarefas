package com.luizeduardobrandao.apptarefas.view.viewholder


import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ItemTaskListBinding
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.service.model.TaskModel

class TaskViewHolder(
    private val itemBinding: ItemTaskListBinding,
    val listener: TaskListener
): RecyclerView.ViewHolder(itemBinding.root) {


    // Atribui valores aos elementos de interface e também eventos
    fun bindData(task: TaskModel){

        itemBinding.textDescription.text = ""
        itemBinding.textPriority.text = ""
        itemBinding.textDueDate.text = ""

        // Eventos
        itemBinding.textDescription.setOnClickListener{ }
        itemBinding.imageTask.setOnClickListener{ }

        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.title_task_removal)
                .setMessage(R.string.label_remove_task)
                .setPositiveButton(R.string.button_yes) { _, _ ->

                }
                .setNeutralButton(R.string.button_cancel, null)
                .show()
            true
        }
    }
}