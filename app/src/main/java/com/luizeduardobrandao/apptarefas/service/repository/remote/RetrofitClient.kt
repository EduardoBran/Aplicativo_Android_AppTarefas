package com.luizeduardobrandao.apptarefas.service.repository.remote

import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// – Centraliza a configuração do Retrofit para chamadas HTTP à sua API remota.
// – Constrói (e reusa) um único objeto Retrofit com base na URL base, no cliente OkHttp
//   e no conversor Gson.
// – O mét0do genérico getService(T::class.java) retorna qualquer interface de serviço Retrofit
//   pronta para ser usada (ex.: TaskService, UserService etc.).

class RetrofitClient private constructor() {

    // Construtor privado para evitar instâncias diretas; usamos apenas o companion object
    companion object {
        // Instância singleton de Retrofit, inicializada somente uma vez
        private lateinit var INSTANCE: Retrofit

        // Variáveis para armazenar os headers de autenticação
        private var token: String = ""
        private var personKey: String = ""


        // * Retorna a instância de Retrofit, criando-a na primeira chamada.
        // * Configura um OkHttpClient com interceptor que injeta os headers
        // * TOKEN_KEY e PERSON_KEY em todas as requisições.
        private fun getRetrofitInstance(): Retrofit {

            // Cria um builder para configurar o cliente HTTP do OkHttp
            // É o item que faz a conexão com a internet
            val httpClient = OkHttpClient.Builder()


            // Interceptor: modifica cada requisição adicionando headers de autenticação
            httpClient.addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                    .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                    .build()
                chain.proceed(request)
            }


            // Se ainda não inicializou INSTANCE, cria dentro de um bloco sincronizado
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("https://www.devmasterteam.com/CursoAndroidAPI/")  // URL base da API
                        .client(httpClient.build())                                 // OkHttpClient com interceptor
                        .addConverterFactory(GsonConverterFactory.create())         // serialização JSON com Gson
                        .build()
                }
            }
            return INSTANCE
        }

        // * Fornece qualquer serviço Retrofit definido por interface.
        // * Exemplo: getService(PersonService::class.java) em "PersonRepository"
        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }

        // * Atualiza os valores de token e personKey usados pelo interceptor.
        // * Deve ser chamado após login/criação ("LoginViewModel") de usuário para que as
        // * próximas requisições tragam os novos headers.
        fun addHeaders(tokenValue: String, personKeyValue: String){
            token = tokenValue
            personKey = personKeyValue
        }
    }
}

// Documentação da API
// https://www.devmasterteam.com/CursoAndroid/API

// Em HTTP, headers (ou “cabeçalhos”) são pares de chave–valor que viajam junto à requisição
// ou à resposta, fornecendo metadados sobre a transação. Eles não fazem parte do corpo (payload)
// propriamente dito, mas descrevem aspectos importantes de como interpretar ou tratar a mensagem.