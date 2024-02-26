package br.com.battlepassCalculatorValorant.model.Properties

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import br.com.battlepassCalculatorValorant.model.GameType.DisputaDeSpike
import br.com.battlepassCalculatorValorant.model.GameType.GameType
import br.com.battlepassCalculatorValorant.model.GameType.SemClassificacao
import br.com.battlepassCalculatorValorant.model.Historic.Historic
import br.com.battlepassCalculatorValorant.model.PassBattle.PassBattle
import br.com.battlepassCalculatorValorant.model.PassBattle.Tier
import java.util.*
import kotlin.collections.ArrayList

class Properties(val historic: Historic, val passBattle: PassBattle) {

    lateinit var semClassificacao: SemClassificacao
    lateinit var disputaDeSpike: DisputaDeSpike

    init {
        semClassificacao = SemClassificacao()
        disputaDeSpike = DisputaDeSpike()
    }

    fun historicTierPositionPerXp(): ArrayList<Int> {
        val mHistoric = ArrayList(historic)
        mHistoric.sortBy { it.tierCurrent }
        val tiersPerXp = ArrayList<Int>()
        var ultimoTier = 0
        var ultimoXp = 0F

        if (!mHistoric.isEmpty()) {
            tiersPerXp.add(0)
            for (tierUserInput in mHistoric) {
                val tierCurrent = passBattle.getTier(tierUserInput.tierCurrent)!!
                val expCurrent =
                    (tierCurrent.expInitial + (tierCurrent.expMissing - tierUserInput.tierExpMissing)).toFloat()

                val diferencaDeTier = tierCurrent.index - ultimoTier
                val diferencaDeXp = expCurrent - ultimoXp
                val razao = diferencaDeXp / diferencaDeTier

                for (t in 1..diferencaDeTier) {
                    val xp = ((razao * t) + ultimoXp).toInt()
                    tiersPerXp.add(xp)
                }
                ultimoTier = tierCurrent.index
                ultimoXp = expCurrent
            }
        }
        return tiersPerXp
    }

    fun getListTiers(): ArrayList<Tier> {
        return passBattle.tiers
    }

    fun getTierCurrent(): Int {
        val ultimoTier: Int = if (historic.isEmpty()) 0 else historic.last().tierCurrent
        return ultimoTier
    }

    fun getTotalXp(): Int {
        val xpPass =
            if (historic.isEmpty()) 0 else passBattle.getTier(historic.last().tierCurrent)!!.expInitial
        val xpCurrent =
            if (historic.isEmpty()) 0 else (passBattle.getTier(historic.last().tierCurrent)!!.expMissing - historic.last().tierExpMissing)
        return xpPass + xpCurrent
    }

    fun getProgressPorcent(): Double {
        val xpCurrent = getTotalXp()
        val xpTotal = passBattle.expTotal
        val num = (xpCurrent.toDouble() / xpTotal) * 100
        val rounded = Math.round(num * 100.0) / 100.0
        return rounded
    }

    fun getXpPerDia(): Int {
        val now = Calendar.getInstance()
        val days = daysApart(now, passBattle.dateInit) + 1
        val xpTotal = getTotalXp()
        val num = (xpTotal.toDouble() / days).toInt()
        return num

    }

    private fun daysApart(d0: Calendar, d1: Calendar): Int {
        var days = d0[Calendar.DAY_OF_YEAR] - d1[Calendar.DAY_OF_YEAR]
        val d1p = Calendar.getInstance()
        d1p.time = d1.time
        while (d1p[Calendar.YEAR] < d0[Calendar.YEAR]) {
            days += d1p.getActualMaximum(Calendar.DAY_OF_YEAR)
            d1p.add(Calendar.YEAR, 1)
        }
        return days
    }

    fun getExpAdiantAtrasado(): Int {
        if (historic.isEmpty()) {
            return 0
        }
        val now = Calendar.getInstance()
        val days = daysApart(now, passBattle.dateInit) + 1
        val daysDurationBattlePass = daysApart(passBattle.dateFinally, passBattle.dateInit) + 1
        val xpPerDay = (passBattle.expTotal.toDouble() / daysDurationBattlePass)
        val xpExpected = days * xpPerDay
        return getTotalXp() - xpExpected.toInt()
    }


    @SuppressLint("SimpleDateFormat")
    fun finishForecast(): String? {
        if (historic.isEmpty()) {
            return "00/00/0000"
        }
        val xpTotal = passBattle.expTotal
        val xpCurrent = getTotalXp()
        val xpDif = xpTotal - xpCurrent
        val totalDays = xpDif.toDouble() / getXpPerDia()
        val dateFinally = passBattle.dateInit.clone() as Calendar
        dateFinally.add(Calendar.DAY_OF_MONTH, totalDays.toInt() + 36)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(dateFinally.time)
    }

    fun daysMissing(): Int? {
        if (historic.isEmpty()) {
            return 0
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val dateFinal = Calendar.getInstance()
        dateFinal.time = sdf.parse(finishForecast())
        return daysApart(passBattle.dateFinally, dateFinal)
    }

    fun percentageTier(): Double {
        val tierUser = if (historic.isEmpty()) null else historic.last()
        if (tierUser != null) {
            val tier = passBattle.getTier(tierUser.tierCurrent)!!
            val total = tier.expMissing
            val percentage = ((total - tierUser.tierExpMissing).toDouble() / total) * 100
            val result = Math.round(percentage * 100).toDouble() / 100
            return result
        } else {
            return 0.toDouble()
        }
    }


    fun dayCurrent(): Int {
        val now = Calendar.getInstance()
        val days = daysApart(now, passBattle.dateInit) + 1
        return days
    }

    fun daysForClosed(): Int {
        val now = Calendar.getInstance()
        val days = daysApart(passBattle.dateFinally, now)
        return days
    }

    fun daysMissingFinalBattlePass(): Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val now = Calendar.getInstance()
        return daysApart(passBattle.dateFinally, now) + 1
    }

    fun jogosRestantes(gameType: GameType): Float {
        val xpTotal = passBattle.expTotal
        val xpCurrent = getTotalXp()
        val xpDif = xpTotal - xpCurrent

        val qGames = (xpDif.toFloat() / gameType.xp)
        return qGames
    }

    fun tempoRestante(gameType: GameType): Float {
        val qGames = jogosRestantes(gameType)
        val time = qGames * gameType.duration
        return time
    }

    fun jogosPorDia(gameType: GameType): Int {
        val days = daysMissingFinalBattlePass()
        val qGames = jogosRestantes(gameType)
        return (qGames / days).toInt() + 1
    }

    fun horasPorDia(gameType: GameType): Float {
        val jogosPorDia = jogosPorDia(gameType)
        val horas = jogosPorDia * gameType.duration
        return horas
    }

}