package com.luizeduardobrandao.apptarefas.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// * Representa a tabela de prioridades no banco de dados.
// * Esta classe é anotada com "@Entity" e será gerenciada pelo Room dentro de "TaskDatabase".
@Entity(tableName = "Priority")
class PriorityModel {

    // Chave primária da tabela, mapeada do JSON "Id".
    @SerializedName("Id")
    @ColumnInfo(name = "id")
    @PrimaryKey // id nao é delegado aqui porque vem via API
    var id: Int = 0

    // Descrição da prioridade, mapeada do JSON "Description".
    @SerializedName("Description")
    @ColumnInfo(name = "description")
    var description: String = ""
}


// Items a serem mapeados:

//    {
//        "Id": 1,
//        "Description": "Baixa"
//    },