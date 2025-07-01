package com.luizeduardobrandao.apptarefas.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.FragmentAllTasksBinding
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.view.adapter.TaskAdapter
import com.luizeduardobrandao.apptarefas.viewmodel.TaskListViewModel

/**
 * A fragment representing a list of Items.
 */
class AllTasksFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModels()
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    private val adapter = TaskAdapter()
    private var taskFilter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)

        // Cria os observadores
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe(){

    }
}