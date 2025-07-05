package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository
import com.luizeduardobrandao.apptarefas.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    // LiveData observado pela activity para lista de prioridades
    // Vem de "list()" de "PriorityRepository"
    val priorityList = priorityRepository.list().asLiveData()

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave


    // Salvando a tarefa na API
    fun save(task: TaskModel) {
        viewModelScope.launch {
            try {
                val response = taskRepository.create(task)
                if (response.isSuccessful && response.body() == true) {
                    _taskSave.value = ValidationModel()
                }
                else {
                    _taskSave.value = errorMessage(response)
                }
            } catch (e: Exception) {
                _taskSave.value = handleException(e)
            }
        }
    }

}