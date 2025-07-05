package com.luizeduardobrandao.apptarefas.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.service.model.ValidationModel
import com.luizeduardobrandao.apptarefas.service.repository.PriorityRepository
import com.luizeduardobrandao.apptarefas.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : BaseAndroidViewModel(application) {

    // Repositórios responsáveis por tarefas e prioridades
    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // Armazena o filtro atual aplicado na listagem de tarefas (TODAS, PRÓXIMAS, ATRASADAS)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _taskDelete = MutableLiveData<ValidationModel>()
    val taskDelete: LiveData<ValidationModel> = _taskDelete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    private val _error = MutableLiveData<ValidationModel>()
    val error: LiveData<ValidationModel> = _error


    // Carrega a lista de tarefas com base no filtro informado (observada em AllTasksFragment)
    // O filtro pode ser: todas, próximas ou atrasadas.
    fun list(filter: Int){
        // atualiza o valor do filtro
        taskFilter = filter

        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                _error.value = handleException(e)
            }
        }
    }


    // Deleta uma tarefa com base no Id (sucesso = lista recarregada, erro = exibicao do erro)
    fun delete(taskId: Int) {
        viewModelScope.launch {
            try {
                val response = taskRepository.delete(taskId)

                if (response.isSuccessful && response.body() == true) {
                    list(taskFilter)
                }
                else {
                    _taskDelete.value = errorMessage(response)
                }
            } catch (e: Exception){
                _taskDelete.value = handleException(e)
            }
        }
    }


    // Atualiza o status da tarefa (completa ou pendente).
    // Após a atualização, recarrega a lista.
    fun status(taskId: Int, complete: Boolean){
        viewModelScope.launch {
            try {
                val response = if (complete){
                    taskRepository.complete(taskId)
                }
                else{
                    taskRepository.undo(taskId)
                }

                if (response.isSuccessful && response.body() == true) {
                    list(taskFilter)
                }
                else {
                    val error = response.errorBody()?.string().orEmpty()
                    _status.value = ValidationModel(error)
                }
            } catch (e: Exception) {
                _status.value = handleException(e)
            }
        }
    }
}