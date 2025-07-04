package com.luizeduardobrandao.apptarefas.service.repository

import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import com.luizeduardobrandao.apptarefas.service.repository.remote.TaskService

// * Repositório responsável por gerenciar as operações relacionadas as tarefas
class TaskRepository {

    // Instância do serviço remoto que define os endpoints da API relacionados à tarefas
    private val remote = RetrofitClient.getService(TaskService::class.java)


    // salva a tarefa na api
    suspend fun save(task: TaskModel){
        remote.create(task.priorityId, task.description, task.dueDate, task.complete)
    }
}