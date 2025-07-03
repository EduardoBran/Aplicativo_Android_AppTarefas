package com.luizeduardobrandao.apptarefas.service.repository.remote

import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// interface com endpoint de task
interface TaskService {

    // Obter lista todas as tarefas sem filtro.
    @GET("Task")
    suspend fun list(): Response<List<TaskModel>>

    // Obter lista todas as tarefas dentro de período de sete dias.
    @GET("Task/Next7Days")
    suspend fun listNext(): Response<List<TaskModel>>

    // Obter lista todas as tarefas expiradas.
    @GET("Task/Overdue")
    suspend fun listOverdue(): Response<List<TaskModel>>

    // Obter tarefa por Id
    // (espera receber um id e "encoded = true" garante que navegador entenda os caracteres passados)
    @GET("Task/{id}")
    suspend fun load(
        @Path(
            value = "id",
            encoded = true
        ) id: Int
    ): Response<TaskModel>

    // Inserção dos dados da tarefa (na documentação espera receber como resposta um booleano)
    @POST("Task")
    @FormUrlEncoded
    suspend fun create(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    // Atualização dos dados da tarefa (na documentação espera receber como resposta um booleano)
    @PUT("Task")
    @FormUrlEncoded
    suspend fun update(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    // Atualiza/marca a tarefa como completa (documentação espera receber como resposta um booleano)
    // (Espera receber um id)
    @PUT("Task/Complete")
    @FormUrlEncoded
    suspend fun complete(
        @Field("Id") id: Int
    ): Response<Boolean>

    // Atualiza/marca a tarefa como incompleta (documentação espera receber como resposta um booleano)
    // (Espera receber um id)
    @PUT("Task/Undo")
    @FormUrlEncoded
    suspend fun undo(
        @Field("Id") id: Int
    ): Response<Boolean>

    // Remoção da tarefa (espera receber um id)
    // - usado "@HTTP" com o méto0o por conta da documentação da api que espera receber um corpo.
    // - path = "Task" - é o caminho (url)
    // - hasBody = true - informando que está indo com corpo.
    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun delete(
        @Field("Id") id: Int
    ): Response<Boolean>
}

// * Item esperados para serem inseridos em "@POST("Task")"

//   Nome	     Valor padrão	    Valor esperado 	     Descrição

// PriorityId	    NULL	           Integer          Id da prioridade
// Description      NULL	           String	        Descrição da tarefa.
//                                                      (Não deve ser maior que 100 caractéres.)
// DueDate	        NULL	           String           Data para finalizar a tarefa.
// Complete	        NULL	           Boolean          Tarefa já foi finalizada ou não.
//                                   (true/false)

// Formatos da String de DueDate: "yyyy-mm-dd" ou "dd-mm-yyyy" e "yyyy/mm/dd" ou "dd/mm/yyyy"


// * Item esperados para serem inseridos em "@PUT("Task")"

//   Nome	     Valor padrão	    Valor esperado 	     Descrição

//    Id            NULL               Integer          Id da tarefa que será atualizada
// PriorityId	    NULL	           Integer          Id da prioridade
// Description      NULL	           String	        Descrição da tarefa.
//                                                      (Não deve ser maior que 100 caractéres.)
//  DueDate	        NULL	           String           Data para finalizar a tarefa.
//  Complete	    NULL	           Boolean          Tarefa já foi finalizada ou não.
//                                   (true/false)

// Formatos da String de DueDate: "yyyy-mm-dd" ou "dd-mm-yyyy" e "yyyy/mm/dd" ou "dd/mm/yyyy"


// * Item esperados para serem inseridos em "@PUT("Task/Complete")", "@PUT("Task/Undo")"
//   e "@HTTP(method = "DELETE", path = "Task", hasBody = true)"

//   Nome	     Valor padrão	    Valor esperado 	     Descrição

//    Id            NULL               Integer           Id da tarefa