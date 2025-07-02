package com.luizeduardobrandao.apptarefas.service.model

import com.google.gson.annotations.SerializedName

// Mapeamento de campos token, personKey e name em PersonModel
class PersonModel {

    @SerializedName("token")
    lateinit var token: String

    @SerializedName("personKey")
    lateinit var personKey: String

    @SerializedName("name")
    lateinit var name: String
}

// {
//    "token": "d+CVvGcmQ+BGHK24T2S9HT7huf33Y8Z630phkYL77F8=",
//    "personKey": "AOanaFzaY+kqXNN56MsmjatpH5y6yrqq1",
//    "name": "user_name"
// }

// Parâmetros a serem mapeados ao fazer Login e Create em "PersonService"

// - A anotação @SerializedName serve para fazer o mapeamento entre o nome da propriedade
//   na sua classe Kotlin e a chave correspondente no JSON que chega (ou sai) da API.