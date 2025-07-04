package com.luizeduardobrandao.apptarefas.service.repository

import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import com.luizeduardobrandao.apptarefas.service.repository.remote.TaskService
import retrofit2.Response

// * Repositório responsável por gerenciar as operações relacionadas as tarefas
class TaskRepository {

    // Instância do serviço remoto que define os endpoints da API relacionados à tarefas
    private val remote = RetrofitClient.getService(TaskService::class.java)

    // Lista todas as tarefas.
    suspend fun list(): Response<List<TaskModel>> {
        return remote.list()
    }

    // Lista as tarefas com vencimento próximo (próximos 7 dias).
    suspend fun listNext(): Response<List<TaskModel>> {
        return remote.listNext()
    }

    // Lista as tarefas vencidas.
    suspend fun listOvedue(): Response<List<TaskModel>> {
        return remote.listOverdue()
    }

    // Cria a tarefa na api.
    suspend fun create(task: TaskModel){
        remote.create(task.priorityId, task.description, task.dueDate, task.complete)
    }
}