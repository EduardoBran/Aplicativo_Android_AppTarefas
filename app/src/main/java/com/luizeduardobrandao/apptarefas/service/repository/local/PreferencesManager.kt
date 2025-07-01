package com.luizeduardobrandao.apptarefas.service.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// – Encapsula o acesso ao DataStore do Android para leitura/gravação de pares chave-valor.
// – Serve para salvar configurações simples ou pequenos flags (por exemplo, tema preferido,
//   token de usuário, última tela visitada).
// – Oferece métodos store(), remove() e get() que abstraem a API de Preferences.
// – Para configurações e pequenos valores de chave-valor que não exigem relacionamentos
//   nem consultas complexas.

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    /**
     * Recebe uma chave e armazena o valor no DataStore
     * */
    suspend fun store(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }
    }

    /**
     * Remove uma chave do DataStore
     * */
    suspend fun remove(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences -> preferences.remove(preferencesKey) }
    }

    /**
     * Faz a leitura do valor associada a chave
     * */
    suspend fun get(key: String): String {
        val preferencesKey = stringPreferencesKey(key)
        val data = context.dataStore.data.first()

        return data[preferencesKey] ?: ""
    }
}