package com.luizeduardobrandao.apptarefas.service.repository

import android.content.Context
import com.luizeduardobrandao.apptarefas.service.model.PriorityModel
import com.luizeduardobrandao.apptarefas.service.repository.local.TaskDatabase
import com.luizeduardobrandao.apptarefas.service.repository.remote.PriorityService
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

// * Repositório responsável por gerenciar as operações relacionadas a lista de prioridades
class PriorityRepository(context: Context) {

    // Instância do serviço remoto que define os endpoints da API relacionados à lista de prioridades
    private val remote = RetrofitClient.getService(PriorityService::class.java)

    // Instância de TaskDatabase
    private val database = TaskDatabase.getDatabase(context).priorityDao()


    // Função para obter a lista de prioridades vinda da API ("PriorityService")
    // Chamada em "LoginViewModel"
    suspend fun listAPI(): Response<List<PriorityModel>> {
        return remote.list()
    }

    // Função para obter a lista de prioridades vinda banco local Room ("PriorityDAO").
    // Chamada em "TaskFormViewModel"
    fun list(): Flow<List<PriorityModel>> {
        return database.list()
    }

    // Obtém a descrição da tarefa, sempre buscando no cache antes do banco de dados
    suspend fun getDescription(id: Int): String {
        return database.getDescription(id)
    }

    // Chama o mét0do "@Query("DELETE FROM Priority") suspend fun clear()" do PriorityDAO.
    // - Isso apaga todos os registros da tabela Priority no seu banco local.
    // Chama o mét0do "@Insert suspend fun save(list: List<PriorityModel>)" do PriorityDAO.
    // - Insere toda a nova lista de PriorityModel — vinda da API — no banco, gravando cada
    //   objeto como uma linha na tabela.
    suspend fun save(list: List<PriorityModel>) {
        // limpa os dados para sempre carregar lista atualizada
        database.clear()
        // carrega lista atualizada
        database.save(list)
    }
}