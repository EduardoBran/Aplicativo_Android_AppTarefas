package com.luizeduardobrandao.apptarefas.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import com.luizeduardobrandao.apptarefas.R
import androidx.navigation.ui.*
import com.luizeduardobrandao.apptarefas.databinding.ActivityMainBinding
import com.luizeduardobrandao.apptarefas.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    // Configuração que define os destinos “raiz” onde exibiremos o ícone de hamburguer
    // e associa esse comportamento a um DrawerLayout da "activity_main.xml".
    private lateinit var appBarConfiguration: AppBarConfiguration

    // ViewBinding preguiçoso: infla "activity_main.xml", que inclui "app_bar_main" e
    // "content_main" (que está incluído em "app_bar_main").
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // Injeta o MainViewModel atrelado ao ciclo de vida da Activity
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define o layout inflado pelo ViewBinding como conteúdo da Activity
        setContentView(binding.root)

        // Configura a Toolbar definida em activity_main.xml como ActionBar
        setSupportActionBar(binding.appBarMain.toolbar)

        // FAB (botão flutuante) definido em app_bar_main.xml: ao clicar, abre TaskFormActivity.
        binding.appBarMain.fab.setOnClickListener {
            startActivity(Intent(applicationContext, TaskFormActivity::class.java))
        }

        // Função para preparar o Navigation Drawer e integração com NavController.
        setupNavigation()

        // Carrega nome do usuário
        viewModel.loadUserName()

        // Função reservada para inscrever observadores do ViewModel.
        observe()
    }

    // Habilita o botão “Up” (seta ou hambúrguer) para trabalhar com NavController.
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Configura o DrawerLayout e o NavigationView para que cliques no menu lateral façam
    // navegação via NavController e tratem logout manualmente.
    private fun setupNavigation() {

        // Referência ao DrawerLayout definido em "activity_main.xml"
        val drawerLayout: DrawerLayout = binding.drawerLayout

        // Referência ao NavigationView (menu lateral) em "activity_main.xml"
        val navView: NavigationView = binding.navView

        // Destinos que exibem ícone de hamburguer ao invés de seta de voltar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_all_tasks,
                R.id.nav_next_tasks,
                R.id.nav_expired
            ), drawerLayout
        )

        // Liga a Toolbar ao NavController, atualizando títulos e ícones automaticamente
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Faz com que itens do NavigationView acionem navegação no NavController
        navView.setupWithNavController(navController)

        // Listener para item de logout no menu lateral
        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logout) {
                // Se for logout, abre LoginActivity e finaliza a atual
                viewModel.logout()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                // Para os demais itens, navega normalmente e fecha o drawer
                NavigationUI.onNavDestinationSelected(it, navController)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }
    }

    // Função adicionar observadores no ViewModel.
    private fun observe() {
        viewModel.name.observe(this) {
            // O parâmetro 0 indica que você quer o primeiro (e normalmente único) cabeçalho
            // da NavigationView.
            val header = binding.navView.getHeaderView(0)
            // Busca dentro de "nav_header_main.xml" de cabeçalho o TextView com id text_name e
            // altera o texto para o nome do usuário.
            header.findViewById<TextView>(R.id.text_name).text = it
        }
    }
}