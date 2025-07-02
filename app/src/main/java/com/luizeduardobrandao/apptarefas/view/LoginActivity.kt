package com.luizeduardobrandao.apptarefas.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.apptarefas.R
import com.luizeduardobrandao.apptarefas.databinding.ActivityLoginBinding
import com.luizeduardobrandao.apptarefas.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // esconde menu superior
        supportActionBar?.hide()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + binding.main.paddingStart, systemBars.top,
                systemBars.right + binding.main.paddingEnd, systemBars.bottom
            )
            insets
        }

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Observadores
        observe()
    }

    // Responde aos eventos de click
    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            // clique do botão login
            handleLogin()
        } else if (v.id == R.id.text_register) {
            // clique em "Cadastre-se" e navega para tela de "RegisterActivity"
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Observa as mudanças nos LiveData expostos pela ViewModel.
    // "status()" e "message()" vem de ValidationModel via ViewModel.
    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Recupera os dados digitados pelo usuário (email e senha) e aciona o processo de login
    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        // delegando os valores para viewmodel tratar estes eventos.
        viewModel.login(email, password)
    }
}

// 1. LoginActivity
// - Usuário clica em “Login” → onClick chama handleLogin().
// - handleLogin() lê email e senha dos campos e chama viewModel.login(email, password).

// 2. LoginViewModel
// - Dentro de uma coroutine (viewModelScope.launch), chama personRepository.login(email, password)
//   e passa os valores.
// - Verifica response.isSuccessful e response.body() != null → atualiza _login.value para
//   true ou false.
//
// 3. PersonRepository
// - Usa o RetrofitClient.getService(PersonService::class.java) para obter PersonService.
// - login(...) delega diretamente para remote.login(...) de PersonService, que é o Retrofit
//   executando a chamada HTTP.

// 4. RetrofitClient & PersonService
// - RetrofitClient cria (e cacheia) o Retrofit configurado com OkHttpClient e GsonConverterFactory.
// - PersonService define os endpoints @POST("Authentication/Login") e @FormUrlEncoded.

// 5. LoginActivity (observe)
// - Observa viewModel.login: LiveData<Boolean>.
// - Se true, abre MainActivity; se false, mostra um Toast de erro.