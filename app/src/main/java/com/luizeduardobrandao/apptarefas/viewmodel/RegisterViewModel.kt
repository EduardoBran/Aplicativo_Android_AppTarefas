package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.exceptions.NoInternetException
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import com.luizeduardobrandao.apptarefas.service.repository.PersonRepository
import com.luizeduardobrandao.apptarefas.service.repository.local.PreferencesManager
import com.luizeduardobrandao.apptarefas.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

// herda de BaseAndroidViewModel (que por sua vez esta herdando AndroidViewModel)
class RegisterViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val personRepository = PersonRepository(application.applicationContext)

    private val _createUser = MutableLiveData<ValidationModel>()
    val createUser: LiveData<ValidationModel> = _createUser

    // Faz criação de registro usando API
    fun create(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = personRepository.create(name, email, password)

                // garantindo que a resposta foi bem sucedida
                if (response.isSuccessful && response.body() != null){

                    // !! serve para dizer ao compilador “confie em mim, aqui esse valor não é nulo”,
                    // após eu mesmo ter garantido isso com a checagem response.body() != null
                    // A variável result agora é não nula e você pode chamar result.token,
                    // result.personKey, etc., sem precisar lidar com null.
                    val result = response.body()!!

                    // Armazena os dados do usuário usando constante de TaskConstants
                    preferencesManager.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                    // Headers que garantem a autenticação do usuário
                    RetrofitClient.addHeaders(result.token, result.personKey)

                    _createUser.value = ValidationModel()
                }
                else {
                    // Esta API já retorna a mensagem de erro específica no formato Json

                    // val msgJson = response.errorBody()?.string().toString()
                    // Converte a mensagem para uma String comum
                    // val msg = Gson().fromJson(msgJson, String::class.java)
                    // _createUser.value = ValidationModel(msg)

                    // "errorMessage()" vem de BaseAndroidViewModel
                    _createUser.value = errorMessage(response)
                }
            } catch (e: NoInternetException) {
                _createUser.value = handleException(e)
            }
        }
    }
}