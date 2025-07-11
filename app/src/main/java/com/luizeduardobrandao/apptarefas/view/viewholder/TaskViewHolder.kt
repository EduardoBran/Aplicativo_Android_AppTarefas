package com.luizeduardobrandao.apptarefas.view.viewholder


import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ItemTaskListBinding
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import java.text.SimpleDateFormat
import java.util.Locale

// * ViewHolder responsável por manter referências às views de cada item e “ligar” os
// * dados de uma TaskModel a essas views.
class TaskViewHolder(
    private val itemBinding: ItemTaskListBinding,
    val listener: TaskListener                      // interface para callbacks de cliques
): RecyclerView.ViewHolder(itemBinding.root) {

    // Formatando a saída da data para um formato melhor (Brasil)
    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Vincula os dados de uma task às views e configura os eventos de clique.
    fun bindData(task: TaskModel){

        // Preenche os campos de texto com os dados da tarefa
        itemBinding.textDescription.text = task.description

        itemBinding.textPriority.text = task.priorityDescription

        // Tratamento para exibição da data corretamente (Brasil)
        val date = inputDateFormat.parse(task.dueDate)
        itemBinding.textDueDate.text = outputDateFormat.format(date!!)

        // val date = inputDateFormat.parse(task.dueDate)
        // itemBinding.textDueDate.text = inputDateFormat.format(date!!)

        // Faz o tratamento para tarefas já completas
        if (task.complete) {
            itemBinding.imageTask.setImageResource(R.drawable.ic_done)
        }
        else {
            itemBinding.imageTask.setImageResource(R.drawable.ic_todo)
        }

        // Clique simples na descrição — por exemplo, para editar ou visualizar detalhes
        // passando o id
        itemBinding.textDescription.setOnClickListener{ listener.onListClick(task.id) }

        // Clique no ícone de tarefa — pode servir para marcar concluída, etc.
        itemBinding.imageTask.setOnClickListener{
            if (task.complete) {
                listener.onUndoClick(task.id)
            }
            else {
                listener.onCompleteClick(task.id)
            }
        }

        // Clique longo na descrição — mostra caixa de diálogo de remoção
        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.title_task_removal)
                .setMessage(R.string.label_remove_task)
                .setPositiveButton(R.string.button_yes) { _, _ ->
                    // deleta a tarefa
                    listener.onDeleteClick(task.id)
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