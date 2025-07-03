package com.luizeduardobrandao.apptarefas.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luizeduardobrandao.apptarefas.service.model.PriorityModel
import kotlinx.coroutines.flow.Flow

// * DAO: define operações de acesso a dados para PriorityModel
// * PriorityDAO fornece métodos para inserir, consultar e limpar registros da tabela 'Priority'.

@Dao
interface PriorityDAO {

    // Insere uma lista de PriorityModel na tabela.
    // Se um id já existir, a operação falhará por violar PK.
    @Insert
    suspend fun save(list: List<PriorityModel>)

    // Retorna um Flow observável com todas as prioridades.
    // Sempre emite lista atualizada automaticamente.
    @Query("SELECT * FROM Priority")
    fun list(): Flow<List<PriorityModel>>

    // Busca apenas a descrição de uma prioridade pelo id.
    // Útil quando só precisamos da string description.
    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getDescription(id: Int): String

    // Remove todas as entradas da tabela 'Priority'.
    @Query("DELETE FROM Priority")
    suspend fun clear()
}

// * Conexão:
// * - PriorityModel define o esquema da tabela.
// * - TaskDatabase inclui PriorityModel e expõe priorityDao().
// * - PriorityDAO usa PriorityModel para mapear colunas nas consultas.