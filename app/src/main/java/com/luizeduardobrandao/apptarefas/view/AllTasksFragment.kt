package com.luizeduardobrandao.apptarefas.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.FragmentAllTasksBinding
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.listener.TaskListener
import com.luizeduardobrandao.apptarefas.view.adapter.TaskAdapter
import com.luizeduardobrandao.apptarefas.viewmodel.TaskListViewModel

// * Fragmento que exibe a lista de tarefas filtradas conforme argumento.
// * Aqui configuramos RecyclerView e observamos o ViewModel.
class AllTasksFragment : Fragment() {

    // Injeta uma instância de TaskListViewModel associada ao ciclo de vida deste Fragment.
    // O delegado `by viewModels()` garante que o ViewModel seja criado apenas uma vez
    // e reutilizado em recriações de view (por exemplo, rotações de tela).
    private val viewModel: TaskListViewModel by viewModels()

    // Referência nula para o binding do layout da Fragment.
    // Será inicializada em onCreateView e liberada em onDestroyView para evitar vazamento de memória.
    private var _binding: FragmentAllTasksBinding? = null

    // Propriedade de acesso não-nula ao binding, que lança se usada após onDestroyView.
    // Facilita o acesso a todas as views sem precisar tratar nulidade a cada uso.
    private val binding get() = _binding!!

    // Adapter que vai gerenciar a exibição da lista de tarefas no RecyclerView.
    // Criado aqui para ser compartilhado entre os métodos do Fragment.
    private val adapter = TaskAdapter()

    // Variável que receberá o critério de filtro de tarefas (todas, próximas ou atrasadas).
    // Inicializada em onCreateView a partir dos argumentos passados ao Fragment.
    private var taskFilter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Infla o layout e inicializa o binding
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        // Define o layout manager (lista vertical) e o adapter no RecyclerView
        // Aqui, binding.recyclerAllTasks já aponta para o RecyclerView do XML
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context)
        binding.recyclerAllTasks.adapter = adapter

        // Lê o filtro passado no Bundle via TaskConstants
        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0)


        // Implementando os membros de "TaskListener"
        val taskListener = object: TaskListener{
            override fun onListClick(id: Int) {
                // clicar em uma tarefa (carrega o TaskFormActivity passando  id)
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)

            }

            override fun onDeleteClick(id: Int) {
                // deletando uma tarefa
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                // passando status de verdadeira para função na TaskListViewModel
                viewModel.status(id, true)
            }

            override fun onUndoClick(id: Int) {
                viewModel.status(id, false)
            }
        }
        adapter.attachListener(taskListener) // chama o listener


        // Configura observadores de LiveData ou StateFlow do ViewModel
        observe()

        // Retorna a raiz do layout
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Chama a lista de tarefas passando o filtro de tarefas e a viewModel lida com a lógica
        // de qual lista ela tem que chamar.
        viewModel.list(taskFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpa o binding para evitar vazamento de memória
        _binding = null
    }
    // Função privada para ligar observadores do ViewModel à UI
    private fun observe(){
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateTasks(it)
        }

        viewModel.taskDelete.observe(viewLifecycleOwner) {
            if (it.status()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_task_removed),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    it.message(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}