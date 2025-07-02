package com.luizeduardobrandao.apptarefas.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// – Centraliza a configuração do Retrofit para chamadas HTTP à sua API remota.
// – Constrói (e reusa) um único objeto Retrofit com base na URL base, no cliente OkHttp
//   e no conversor Gson.
// – O mét0do genérico getService(T::class.java) retorna qualquer interface de serviço Retrofit
//   pronta para ser usada (ex.: TaskService, UserService etc.).

class RetrofitClient private constructor() {

    companion object {
        private lateinit var INSTANCE: Retrofit

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            if (!Companion::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("https://www.devmasterteam.com/CursoAndroidAPI/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }
    }
}

// Documentação da API
// https://www.devmasterteam.com/CursoAndroid/API