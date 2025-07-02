package com.luizeduardobrandao.apptarefas.service.repository.remote

import com.luizeduardobrandao.apptarefas.service.model.PersonModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// interface com endpoints de Login e Create
interface PersonService {

    // Segundo a documentação da api o Login espera 'email' e 'password'
    // precisaremos mapeas esses atributos na função e passar a url segundo a documentação
    @POST("Authentication/Login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<PersonModel>

    // Segundo a documentação da api o Create espera 'name', 'email' e 'password'
    @POST("Authentication/Create")
    @FormUrlEncoded
    suspend fun create(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<PersonModel>
}

// - @FormUrlEncoded @FormUrlEncoded indica que o corpo da requisição será enviado como
//   "application/x-www-form-urlencoded" (não JSON), ou seja, codificado em formulário.
//   É a maneira de enviar dados usando o @POST.

// - @Field indica qual é o parâmetro que API vai receber

// - Verificar na documentação que a resposta para a solicitação de Login e Create será este json:

// {
//    "token": "d+CVvGcmQ+BGHK24T2S9HT7huf33Y8Z630phkYL77F8=",
//    "personKey": "AOanaFzaY+kqXNN56MsmjatpH5y6yrqq1",
//    "name": "user_name"
// }

// - Levar estes parâmetros para PersonModel para serem mapeados.

// - Login e Create respondem com os mesmos parâemtros, por isso foi usado PersonModel nos dois.

// - Em Retrofit com o adaptador de coroutines do Kotlin, você marca os métodos da interface
//   como "suspend" para que eles sejam chamados dentro de uma coroutine e não bloqueiem
//   a thread principal