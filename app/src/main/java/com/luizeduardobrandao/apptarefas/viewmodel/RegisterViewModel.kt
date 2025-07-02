package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import com.luizeduardobrandao.apptarefas.service.repository.PersonRepository
import com.luizeduardobrandao.apptarefas.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val personRepository = PersonRepository()

    private val _createUser = MutableLiveData<ValidationModel>()
    val createUser: LiveData<ValidationModel> = _createUser

    // Faz criação de registro usando API
    fun create(name: String, email: String, password: String) {
        viewModelScope.launch {
            val response = personRepository.create(name, email, password)

            // garantindo que a resposta foi bem sucedida
            if (response.isSuccessful && response.body() != null){
                _createUser.value = ValidationModel()
            }
            else {
                // Esta API já retorna a mensagem de erro específica no formato Json
                val msgJson = response.errorBody()?.string().toString()

                // Converte a mensagem para uma String comum
                val msg = Gson().fromJson(msgJson, String::class.java)

                _createUser.value = ValidationModel(msg)
            }
        }
    }
}