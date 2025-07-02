package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.repository.PersonRepository
import com.luizeduardobrandao.apptarefas.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val personRepository = PersonRepository()

    private val _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> = _login

    // Faz login usando API
    fun login(email: String, password: String){
        viewModelScope.launch {
            val response = personRepository.login(email, password)

            // garantindo que a resposta foi bem sucedida
            if (response.isSuccessful && response.body() != null) {
                _login.value = true
            }
            else {
                _login.value = false
            }
        }
    }
}