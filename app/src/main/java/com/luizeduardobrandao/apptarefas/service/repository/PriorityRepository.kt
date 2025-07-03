package com.luizeduardobrandao.apptarefas.service.repository

import android.content.Context
import com.luizeduardobrandao.apptarefas.service.model.PriorityModel
import com.luizeduardobrandao.apptarefas.service.repository.local.TaskDatabase
import com.luizeduardobrandao.apptarefas.service.repository.remote.PriorityService
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import retrofit2.Response

// * Repositório responsável por gerenciar as operações relacionadas a lista de prioridades
class PriorityRepository(context: Context) {

    // Instância do serviço remoto que define os endpoints da API relacionados à lista de prioridades
    private val remote = RetrofitClient.getService(PriorityService::class.java)

    // Instância de TaskDatabase
    private val database = TaskDatabase.getDatabase(context).priorityDao()

    // Função para obter a lista de prioridades da API
    suspend fun listAPI(): Response<List<PriorityModel>> {
        return remote.list()
    }

    // Cria uma nova tarefa
    suspend fun save(list: List<PriorityModel>) {
        // limpa os dados para sempre carregar lista atualizada
        database.clear()
        // carrega lista atualizada
        database.save(list)
    }
}