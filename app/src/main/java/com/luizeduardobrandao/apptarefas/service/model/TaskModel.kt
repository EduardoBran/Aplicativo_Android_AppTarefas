package com.luizeduardobrandao.apptarefas.service.model

import com.google.gson.annotations.SerializedName

// Mapeamento de uma Lista de Tarefas (ainda sem salvar no banco de dados)
data class TaskModel (

    @SerializedName("Id")
    val id: Int,

    @SerializedName("PriorityId")
    val priorityId: Int,

    @SerializedName("Description")
    val description: String,

    @SerializedName("DueDate")
    val dueDate: String,

    @SerializedName("Complete")
    val complete: Boolean,

    // novo atributo para passar a descrição da prioridade (obrigatório passar um valor)
    var priorityDescription: String = ""
)

// Items a serem mapeados:

//    {
//        "Id": 2,
//        "PriorityId": 1,
//        "Description": "Descrição",
//        "DueDate": "2019-06-25",
//        "Complete": true
//    }