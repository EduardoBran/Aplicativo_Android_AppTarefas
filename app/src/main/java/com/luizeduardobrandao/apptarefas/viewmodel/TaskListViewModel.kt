package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    // Repositórios responsáveis por tarefas
    private val taskRepository = TaskRepository()

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks


    // Carrega a lista de tarefas (a ser observada em AllTasksFragment)
    fun list(){
        viewModelScope.launch {
            val response = taskRepository.list()
            if (response.isSuccessful && response.body() != null){
                val result = response.body()!!
                _tasks.value = result
            }
        }
    }
}