package com.luizeduardobrandao.apptarefas.service.repository

import com.luizeduardobrandao.apptarefas.service.model.PersonModel
import com.luizeduardobrandao.apptarefas.service.repository.remote.PersonService
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import retrofit2.Response

// * Repositório responsável por gerenciar as operações relacionadas ao usuário (pessoa),
// * como login e cadastro, utilizando chamadas de API remotas.
class PersonRepository {

    // Instância do serviço remoto que define os endpoints da API relacionados à pessoa
    private val remote = RetrofitClient.getService(PersonService::class.java)

    // Realiza o login de um usuário utilizando e-mail e senha.
    suspend fun login(email: String, password: String): Response<PersonModel>{

        return remote.login(email, password)
    }

    // Realiza o cadastro de um usuário utilizando e-mail, senha e nome.
    suspend fun create(name: String, email: String, password: String): Response<PersonModel>{

        return remote.create(name, email, password)
    }
}