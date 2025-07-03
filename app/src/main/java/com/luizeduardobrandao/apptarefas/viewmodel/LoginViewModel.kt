package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import com.luizeduardobrandao.apptarefas.service.repository.PersonRepository
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository
import com.luizeduardobrandao.apptarefas.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

// herda de BaseAndroidViewModel (que por sua vez esta herdando AndroidViewModel)
class LoginViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val personRepository = PersonRepository()

    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    // Faz login usando API
    fun login(email: String, password: String){
        viewModelScope.launch {
            val response = personRepository.login(email, password)

            // garantindo que a resposta foi bem sucedida
            if (response.isSuccessful && response.body() != null) {

                // !! serve para dizer ao compilador “confie em mim, aqui esse valor não é nulo”,
                // após eu mesmo ter garantido isso com a checagem response.body() != null
                // A variável result agora é não nula e você pode chamar result.token,
                // result.personKey, etc., sem precisar lidar com null.
                val result = response.body()!!

                // Armazena os dados do usuário usando constante de TaskConstants
                preferencesManager.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                preferencesManager.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                preferencesManager.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                _login.value = ValidationModel()
            }
            else {
                // Esta API já retorna a mensagem de erro específica no formato Json

                // val msgJson = response.errorBody()?.string().toString()
                // Converte a mensagem para uma String comum
                // val msg = Gson().fromJson(msgJson, String::class.java)

                _login.value = errorMessage(response)
            }
        }
    }

    // Verifica se usuário está logado através da DataStore
    fun verifyLoggedUser(){
        viewModelScope.launch {
            val token = preferencesManager.get(TaskConstants.SHARED.TOKEN_KEY)
            val person = preferencesManager.get(TaskConstants.SHARED.PERSON_KEY)

            // Se token e person key forem diferentes de vazio, usuário está logado
            val logged = (token != "" && person != "")
            _loggedUser.value = logged

            // Se usuário não estiver logado, aplicação vai atualizar/baixar os dados da API
            if (!logged){
                val response = priorityRepository.listAPI()
                if (response.isSuccessful && response.body() != null){
                    // busca "save()" em "PriorityRepository"
                    priorityRepository.save(response.body()!!)
                }
            }
        }
    }
}