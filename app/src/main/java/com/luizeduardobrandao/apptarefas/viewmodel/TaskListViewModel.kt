package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository
import com.luizeduardobrandao.apptarefas.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    // Repositórios responsáveis por tarefas e prioridades
    private val taskRepository = TaskRepository()
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // Armazena o filtro atual aplicado na listagem de tarefas (TODAS, PRÓXIMAS, ATRASADAS)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks


    // Carrega a lista de tarefas com base no filtro informado (observada em AllTasksFragment)
    // O filtro pode ser: todas, próximas ou atrasadas.
    fun list(filter: Int){
        // atualiza o valor do filtro
        taskFilter = filter

        viewModelScope.launch {
            val response = when (filter) {
                TaskConstants.FILTER.ALL -> taskRepository.list()
                TaskConstants.FILTER.NEXT -> taskRepository.listNext()
                else -> taskRepository.listOvedue()
            }


            if (response.isSuccessful && response.body() != null){
                val result = response.body()!!

            result.map { task ->
                task.priorityDescription = priorityRepository.getDescription(task.priorityId)
            }

                _tasks.value = result
            }
        }
    }

    // Atualiza o status da tarefa (completa ou pendente).
    // Após a atualização, recarrega a lista.
    fun status(taskId: Int, complete: Boolean){
        viewModelScope.launch {
            val response = if (complete) {
                taskRepository.complete(taskId)
            }
            else {
                taskRepository.undo(taskId)
            }

            if (response.isSuccessful && response.body() != null){
                // atualiza a lista
                list(taskFilter)
            }
        }
    }
}