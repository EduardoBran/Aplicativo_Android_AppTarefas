package com.luizeduardobrandao.apptarefas.service.repository.remote

import com.luizeduardobrandao.apptarefas.service.model.PriorityModel
import retrofit2.Response
import retrofit2.http.GET

// interface com endpoint de priority
interface PriorityService {

    // Obter lista de prioridades.
    @GET("Priority")
    suspend fun list(): Response<List<PriorityModel>>
}