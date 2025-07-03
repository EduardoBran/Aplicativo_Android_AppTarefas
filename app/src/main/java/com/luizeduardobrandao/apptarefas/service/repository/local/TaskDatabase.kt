package com.luizeduardobrandao.apptarefas.service.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luizeduardobrandao.apptarefas.service.model.PriorityModel

// – Define a sua camada de banco de dados local relacional usando Room.
// – É a classe abstrata que herda de RoomDatabase e expõe (quando você adicionar) DAOs para
//   inserir, atualizar, excluir e consultar entidades (por exemplo, tarefas).
// – O mét0do getDatabase(context) garante uma única instância (singleton) de TaskDatabase
//   em toda a app.
// - Para coleções de dados estruturados, com várias colunas, chaves primárias, relacionamentos,
//   e que você queira consultar, filtrar, ordenar ou observar via Flow/LiveData.

// * TaskDatabase é o ponto de acesso ao banco de dados Room.
// * Define as entidades e versão do schema.
@Database(
    entities = [PriorityModel::class],  // Lista de entidades (tabelas) gerenciadas por este banco
    version = 1                         // Versão atual do esquema do banco de dados
)
abstract class TaskDatabase : RoomDatabase() {

    // * Fornece acesso ao "PriorityDAO" para operações sobre "PriorityModel".
    //     * O mét0do é declarado como abstrato para que o Room gere automaticamente a
    //     * implementação em tempo de compilação.
    //     * Quando o banco é construído, o Room fornece uma instância concreta desse DAO,
    //     * permitindo inserir, consultar e limpar registros da tabela 'Priority'.
    abstract fun priorityDao(): PriorityDAO

    // * Instância singleton de TaskDatabase.
    companion object {
        // * Lateinit garante inicialização tardia, evitando nulos.
        private lateinit var INSTANCE: TaskDatabase

        // * Retorna a instância de TaskDatabase.
        //  * Se ainda não existir, cria de forma thread-safe.
        fun getDatabase(context: Context): TaskDatabase {

            // Verifica se a instância já foi inicializada
            if (!Companion::INSTANCE.isInitialized) {
                // Garante que apenas uma thread crie a instância
                synchronized(TaskDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,                     // Contexto da aplicação
                        TaskDatabase::class.java,    // Classe do banco de dados
                        "tasksDB"              // Nome do arquivo de banco
                    )
                        // Permite consultas na thread principal (desaconselhado em produção)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}