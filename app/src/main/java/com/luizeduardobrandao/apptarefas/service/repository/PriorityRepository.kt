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


    // Mecanismo de cache (evita acesso a banco de dados ou api que não são necessários)
    companion object {
        // Cache de descrições em memória. (Chave: priorityId, Valor: descrição correspondente)
        private val descriptionCache = mutableMapOf<Int, String>()

        // Retorna a descrição em cache para um dado ID, ou null se não estiver cacheada.
        private fun getCachedDescription(id: Int): String? = descriptionCache[id]

        // Armazena no cache a descrição para um dado ID.
        private fun setCachedDescription(id: Int, description: String){
            descriptionCache[id] = description
        }
    }

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

    // Obtém a descrição da tarefa, (sempre buscando no cache antes do banco de dados)
    suspend fun getDescription(id: Int): String {
        // 1) Tenta ler do cache
        getCachedDescription(id)?.let { cachedDesc ->
            return cachedDesc
        }

        // 2) se não existir no cache, buscamos no DAO (Room)
        val descriptionFromDb = database.getDescription(id)

        // 3) armazenamos no cache para próximas chamadas
        setCachedDescription(id, descriptionFromDb)

        // 4) retornamos o valor obtido
        return descriptionFromDb
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