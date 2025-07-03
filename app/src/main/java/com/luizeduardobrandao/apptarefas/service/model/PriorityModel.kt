package com.luizeduardobrandao.apptarefas.service.model

import com.google.gson.annotations.SerializedName

// Mapeamento de uma Lista de Prioridades (ainda sem salvar no banco de dados)
data class PriorityModel (
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Description")
    val description: String
)


// Items a serem mapeados:

//    {
//        "Id": 1,
//        "Description": "Baixa"
//    },