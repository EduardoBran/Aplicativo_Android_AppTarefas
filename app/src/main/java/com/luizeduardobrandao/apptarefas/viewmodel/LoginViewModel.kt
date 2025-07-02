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

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val personRepository = PersonRepository()

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    // Faz login usando API
    fun login(email: String, password: String){
        viewModelScope.launch {
            val response = personRepository.login(email, password)

            // garantindo que a resposta foi bem sucedida
            if (response.isSuccessful && response.body() != null) {
                _login.value = ValidationModel()
            }
            else {
                // Esta API já retorna a mensagem de erro específica no formato Json
                val msgJson = response.errorBody()?.string().toString()

                // Converte a mensagem para uma String comum
                val msg = Gson().fromJson(msgJson, String::class.java)

                _login.value = ValidationModel(msg)
            }
        }
    }
}