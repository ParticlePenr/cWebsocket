package br.com.battlepassCalculatorValorant.model.dto

data class PrevisoesJogos(
    var jogosRestantes: Float = 0F,
    var tempoRestante: String = "",
    var jogosPorDia: Float = 0F,
    var horasPorDia: String = ""
)
