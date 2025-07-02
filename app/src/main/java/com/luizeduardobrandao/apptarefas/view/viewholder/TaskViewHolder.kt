package com.luizeduardobrandao.apptarefas.view.viewholder


import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ItemTaskListBinding
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.service.model.TaskModel

// * ViewHolder responsável por manter referências às views de cada item e “ligar” os
// * dados de uma TaskModel a essas views.
class TaskViewHolder(
    private val itemBinding: ItemTaskListBinding,
    val listener: TaskListener                      // interface para callbacks de cliques
): RecyclerView.ViewHolder(itemBinding.root) {

    // Vincula os dados de uma task às views e configura os eventos de clique.
    fun bindData(task: TaskModel){

        // Preenche os campos de texto com os dados da tarefa
        itemBinding.textDescription.text = ""
        itemBinding.textPriority.text = ""
        itemBinding.textDueDate.text = ""

        // Clique simples na descrição — por exemplo, para editar ou visualizar detalhes
        itemBinding.textDescription.setOnClickListener{ }

        // Clique no ícone de tarefa — pode servir para marcar concluída, etc.
        itemBinding.imageTask.setOnClickListener{ }

        // Clique longo na descrição — mostra caixa de diálogo de remoção
        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.title_task_removal)
                .setMessage(R.string.label_remove_task)
                .setPositiveButton(R.string.button_yes) { _, _ ->

                }
                .setNeutralButton(R.string.button_cancel, null)
                .show()
            // indica que consumimos o evento
            true
        }
    }
}

// - ItemTaskListBinding: gerado pelo View Binding; agrupa todas as views do layout
//   item_task_list.xml.
// - TaskListener: interface que sua Activity/Fragment implementa para reagir a cliques
//   (editar, excluir, etc.).
// - bindData(): recebe um TaskModel e:
//     - Preenche textDescription, textPriority e textDueDate.
//     - Configura cliques simples e prolongados, delegando ação para o listener.