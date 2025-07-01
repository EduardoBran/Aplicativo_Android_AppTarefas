package com.luizeduardobrandao.apptarefas.service.repository.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

// – Define a sua camada de banco de dados local relacional usando Room.
// – É a classe abstrata que herda de RoomDatabase e expõe (quando você adicionar) DAOs para
//   inserir, atualizar, excluir e consultar entidades (por exemplo, tarefas).
// – O mét0do getDatabase(context) garante uma única instância (singleton) de TaskDatabase
//   em toda a app.
// - Para coleções de dados estruturados, com várias colunas, chaves primárias, relacionamentos,
//   e que você queira consultar, filtrar, ordenar ou observar via Flow/LiveData.

//@Database(entities = [PriorityModel::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    companion object {
        private lateinit var INSTANCE: TaskDatabase

        fun getDatabase(context: Context): TaskDatabase {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(TaskDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, TaskDatabase::class.java, "tasksDB")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}