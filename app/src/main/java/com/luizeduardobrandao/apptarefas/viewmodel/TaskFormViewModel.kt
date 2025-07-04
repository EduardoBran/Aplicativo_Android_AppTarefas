package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)

    // LiveData observado pela activity para lista de prioridades
    // Vem de "list()" de "PriorityRepository"
    val priorityList = priorityRepository.list().asLiveData()

}