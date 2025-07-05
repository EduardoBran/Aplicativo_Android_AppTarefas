package com.luizeduardobrandao.apptarefas.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.service.exceptions.NoInternetException
import retrofit2.Response

// Repositório base que faz a verificação do acesos a internet
// (todos os outros repositórios vão herdar dele)
open class BaseRepository(val context: Context) {

    // Recebe uma função por parâmetro que é executada somente se existe conexão com a internet.
    suspend fun <T> safeApiCall(apiCall: suspend() -> Response<T>): Response<T> {
        if (!isConnectionAvailable()){
            // 1) antes de tocar na API, verifica se há rede
            throw NoInternetException(context.getString(R.string.error_internet_connection))
        }
        // 2) se ok, executa a chamada Retrofit passada como lambda
        return apiCall()
    }

    // chama a classe NoInternetException


    // Função para verificar se existe conexão com internet
    private fun isConnectionAvailable(): Boolean {

        // assume que o resultado é falso
        var result = false

        // Gerenciador de conexão
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Verifica versão do sistema rodando a aplicação e aplica o tratamento
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = cm.activeNetwork ?: return false
            val netWorkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false

            result = when {
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else {
            if (cm.activeNetworkInfo != null) {
                result = when (cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }
}