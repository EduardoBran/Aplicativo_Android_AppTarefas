package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.service.exceptions.NoInternetException
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import retrofit2.Response

/**
 * ViewModel base que provê funcionalidades comuns para outras ViewModels do aplicativo.
 *
 * Esta classe fornece métodos auxiliares para tratamento de erros genéricos,
 * como exceções e respostas de erro da API, além de facilitar o acesso ao contexto da aplicação.
 */
open class BaseAndroidViewModel(private val app: Application): AndroidViewModel(app) {

    // Extrai a mensagem de erro da resposta da API e encapsula em um ValidationModel.
    // "Response<T>" faz a resposta ser de qualquer tipo (genérica).
    fun <T> errorMessage(response: Response<T>): ValidationModel {
        return ValidationModel(
            Gson().fromJson(
                response.errorBody()?.string().toString(), String::class.java
            )
        )
    }

    // * Trata exceções lançadas durante chamadas de rede.
    // * Caso a exceção seja do tipo NoInternetException, retorna uma mensagem específica.
    // * Caso contrário, retorna uma mensagem genérica de erro inesperado.
    fun handleException(e: Exception): ValidationModel {
        return if (e is NoInternetException){
            ValidationModel(e.errorMessage)
        }
        else {
            ValidationModel(app.applicationContext.getString(R.string.error_unexpected))
        }
    }
}