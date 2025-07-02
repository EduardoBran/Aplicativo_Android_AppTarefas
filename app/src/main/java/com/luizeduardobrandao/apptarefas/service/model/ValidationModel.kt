package com.luizeduardobrandao.apptarefas.service.model

// * A classe ValidationModel tem como objetivo encapsular o resultado de uma operação de
//   validação, reunindo num único objeto:
// * Um flag de sucesso/falha (status)
// * Uma mensagem de validação (validationMessage) — normalmente usada para exibir um
//   erro específico ao usuário.
class ValidationModel(message: String = "") {

    // true por padrão, assume sucesso até provar o contrário
    private var status: Boolean = true
    // mensagem de erro (vazia em caso de sucesso)
    private var errorMessage = ""

    // O bloco init é executado logo após os atributos serem inicializados.
    // Aqui verificamos se foi passada alguma mensagem de erro:
    // - Se message for não-vazio, significa que houve falha na validação.
    // - Então armazenamos essa mensagem em validationMessage
    //   e marcamos status = false.
    init {
        if (message.isNotEmpty()) {
            status = false
            errorMessage = message
        }
    }

    // Retorna true se não houve erro (message == ""), false caso contrário.
    fun status() = status

    // Retorna a mensagem de erro, ou string vazia se status == true.
    fun message() = errorMessage
}