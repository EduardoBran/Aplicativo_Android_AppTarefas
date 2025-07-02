package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)

    // Remove os dados do usuário armazenados nas preferências, efetivando o logout.
    fun logout(){
        viewModelScope.launch {
            preferencesManager.remove(TaskConstants.SHARED.TOKEN_KEY)
            preferencesManager.remove(TaskConstants.SHARED.PERSON_KEY)
            preferencesManager.remove(TaskConstants.SHARED.PERSON_NAME)
        }
    }
}