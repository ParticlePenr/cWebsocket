package br.com.samuelnunes.valorantpassbattle.model.dto

data class Reward(
    val id: Int,
    val nome: String,
    val tipo: String,
    val imagens: List<String>,
    val cap: Int? = null
)