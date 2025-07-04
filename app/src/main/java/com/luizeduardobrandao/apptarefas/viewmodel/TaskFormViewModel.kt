package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository
import com.luizeduardobrandao.apptarefas.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository()

    // LiveData observado pela activity para lista de prioridades
    // Vem de "list()" de "PriorityRepository"
    val priorityList = priorityRepository.list().asLiveData()

    // Salvando a tarefa na API
    fun save(task: TaskModel) {
        viewModelScope.launch {
            val response = taskRepository.save(task)
        }
    }

}