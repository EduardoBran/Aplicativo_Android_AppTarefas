package com.luizeduardobrandao.apptarefas.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ActivityTaskFormBinding
import com.luizeduardobrandao.apptarefas.service.constants.TaskConstants
import com.luizeduardobrandao.apptarefas.service.model.PriorityModel
import com.luizeduardobrandao.apptarefas.service.model.TaskModel
import com.luizeduardobrandao.apptarefas.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private val viewModel: TaskFormViewModel by viewModels()
    private val binding: ActivityTaskFormBinding by lazy {
        ActivityTaskFormBinding.inflate(
            layoutInflater
        )
    }

    // usado para formatar a data
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // variável de instância da TaskFormActivity que guarda a lista completa de objetos
    // "PriorityModel" recuperada do banco.
    private var listPriority: List<PriorityModel> = mutableListOf()

    // variável para recuperar o Id passado por AllTasksFragment
    private var taskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura o botão de back no header (menu)
        binding.buttonBack.setOnClickListener {
            finish()
        }

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        // Carrega dados da Activity (AllTasksFragment) se existirem
        loadDataFromActivity()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        // Quando o usuário clica no botão de data, chama handleDate()
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    // É chamado automaticamente pelo sistema após o usuário escolher a data no
    // DatePickerDialog de handleSave()
    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        // Monta um Calendar para converter os valores em um objeto Date
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        // Formata a data usando o SimpleDateFormat definido na classe
        val dueDate = dateFormat.format(calendar.time)
        // Atualiza o texto do botão para exibir a data escolhida
        binding.buttonDate.text = dueDate
    }

    private fun observe() {
        // Populando o spinner
        viewModel.priorityList.observe(this){
            // É a lista de PriorityModel que vem do LiveData (priorityList) do TaskFormViewModel.
            listPriority = it
            // criando a lista
            val list = mutableListOf<String>()
            for (item in it) {
                list.add(item.description)
            }

            // adapter
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )

            // Populando
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this) {
            if (it.status()) {
                if (taskId == 0){
                    toast(getString(R.string.msg_task_created))
                }
                else {
                    toast(getString(R.string.msg_task_updated))
                }
                finish()
            }
            else {
                toast(it.message())
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
            binding.buttonSave.text = getString(R.string.button_update_task) // atualiza texto botao
        }

        viewModel.taskLoad.observe(this){
            if (!it.status()) {
                toast(it.message())
                finish()
            }
        }
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    // Carregar id da tarefa (se existir)
    // Caso existe, trata-se de edição
    private fun loadDataFromActivity(){
        val bundle = intent.extras
        // verificando se já existem dados no bundle (quando cria não existe dados ainda)
        if (bundle != null){
            taskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskId)
        }
    }

    // Retorna o índice correspondente ao ID de prioridade fornecido.
    // Utilizado para selecionar o item correto no Spinner.
    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (l in listPriority){
            if (l.id == priorityId){
                break
            }
            index++
        }
        return index
    }


    // Coleta os dados preenchidos no formulário, cria um modelo da tarefa e chama a ViewModel
    // para salvar os dados.
    private fun handleSave() {

        // Busca a descrição da tarefa
        val description = binding.editDescription.text.toString()

        // Busca o índice (posição) do item que o usuário escolheu no Spinner.
        // Ex.: 0 para o primeiro, 1 para o segundo, e assim por diante.
        val priorityId = listPriority[binding.spinnerPriority.selectedItemPosition].id

        // Busca o valor do CheckBox "Completa"
        val completed = binding.checkComplete.isChecked

        // Busca o texto do botão de data que foi atualizado em onDateSet
        val dueDate = binding.buttonDate.text.toString()

        // Passa os valores para o TaskModel
        val task = TaskModel(taskId, priorityId, description, dueDate, completed)
        viewModel.save(task)
    }

    // Abre o diálogo de seleção de data na tela.
    private fun handleDate() {
        // Exibe a data atual como valores iniciais
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        // Criamos e mostramos o DatePickerDialog, passando 'this' como listener
        // O diálogo automaticamente exibe a roda de seleção de dia, mês e ano
        DatePickerDialog(
            this,  // Contexto da Activity
            this,  // OnDateSetListener: a própria Activity implementa a interface
            year,         // ano inicial selecionado
            month,        // mês inicial selecionado (0 = janeiro)
            day           // dia inicial selecionado
        ).show()
    }
}