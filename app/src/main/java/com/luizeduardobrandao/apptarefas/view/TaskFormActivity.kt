package com.luizeduardobrandao.apptarefas.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ActivityTaskFormBinding
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

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var listPriority: List<PriorityModel> = mutableListOf()
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

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        observe()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    private fun observe() {
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun handleSave() {
        // val task = TaskModel()
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }
}