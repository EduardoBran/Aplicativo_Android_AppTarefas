package com.luizeduardobrandao.apptarefas.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.apptarefas.databinding.ItemTaskListBinding
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.view.viewholder.TaskViewHolder

// * Adapter que fornece ViewHolders para o RecyclerView e mantém a lista de tarefas.
class TaskAdapter: RecyclerView.Adapter<TaskViewHolder>() {

    // Lista que armazena as tarefas a serem exibidas pelo Adapter.
    // Inicializada como uma lista vazia para evitar nulidade.
    // Sempre que você chamar updateTasks(), esse campo será sobrescrito com novos dados.
    private var listTasks: List<TaskModel> = arrayListOf()

    // Listener para capturar cliques nos itens da lista.
    // Declarado com “lateinit” pois será atribuído (via attachListener) antes que o
    // RecyclerView tente renderizar os itens.
    private lateinit var listener: TaskListener

    // Quando o RecyclerView precisa criar um novo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemTaskListBinding.inflate(inflater, parent, false)
        // Passa o listener para o ViewHolder permitir callbacks
        return TaskViewHolder(itemBinding, listener)
    }

    // Número de itens na lista
    override fun getItemCount(): Int {
        return listTasks.count()
    }

    // Quando o RecyclerView vai “vincular” dados ao ViewHolder existente
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    // Atualiza a lista de tarefas e notifica o RecyclerView para redesenhar tudo.
    fun updateTasks(list: List<TaskModel>){
        listTasks = list
        notifyDataSetChanged()
    }

    // Recebe a implementação de TaskListener (geralmente da Fragment AllTasksFragment)
    // para que ViewHolders possam disparar callbacks.
    fun attachListener(taskListener: TaskListener){
        listener = taskListener
    }
}

// - listTasks: armazena o List<TaskModel> atual.
// - attachListener(): deve ser chamado antes de atribuir o adapter ao RecyclerView,
//   passando a interface de callbacks.
// - notifyDataSetChanged(): atualiza toda a lista.