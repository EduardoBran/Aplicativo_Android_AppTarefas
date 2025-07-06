package com.luizeduardobrandao.apptarefas.service.repository

import android.content.Context
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import com.luizeduardobrandao.apptarefas.service.repository.remote.TaskService
import retrofit2.Response

// * Repositório responsável por gerenciar as operações relacionadas as tarefas
class TaskRepository(context: Context): BaseRepository(context) {

    // Instância do serviço remoto que define os endpoints da API relacionados à tarefas
    private val remote = RetrofitClient.getService(TaskService::class.java)

    // Lista todas as tarefas.
    suspend fun list(): Response<List<TaskModel>> {
        return safeApiCall { remote.list() }
    }

    // Lista as tarefas com vencimento próximo (próximos 7 dias).
    suspend fun listNext(): Response<List<TaskModel>> {
        return safeApiCall { remote.listNext() }
    }

    // Lista as tarefas vencidas.
    suspend fun listOvedue(): Response<List<TaskModel>> {
        return safeApiCall { remote.listOverdue() }
    }

    // Cria a tarefa na api.
    suspend fun create(task: TaskModel): Response<Boolean>{
        return safeApiCall {
            remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        }
    }

    // Atualiza uma tarefa existente.
    suspend fun update(task: TaskModel): Response<Boolean> {
        return safeApiCall {
            remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        }
    }

    // Carrega uma tarefa específica pelo ID.
    suspend fun load(id: Int): Response<TaskModel> {
        return safeApiCall { remote.load(id) }
    }

    // Remove uma tarefa pelo ID
    suspend fun delete(id: Int): Response<Boolean>{
        return safeApiCall { remote.delete(id) }
    }

    // Marca uma tarefa como completa.
    suspend fun complete(id: Int): Response<Boolean>{
        return safeApiCall { remote.complete(id) }
    }

    // Desfaz a conclusão de uma tarefa.
    suspend fun undo(id: Int): Response<Boolean>{
        return safeApiCall { remote.undo(id) }
    }
}