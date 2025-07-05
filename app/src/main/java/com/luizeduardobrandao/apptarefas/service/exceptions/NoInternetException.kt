package com.luizeduardobrandao.apptarefas.service.exceptions

// * Essa classe é uma exceção personalizada que serve para sinalizar, de forma semântica,
// * um erro de falta de conexão à Internet
class NoInternetException(val errorMessage: String): Exception(errorMessage)