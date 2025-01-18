package br.com.battlepassCalculatorValorant.model.Properties

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import br.com.battlepassCalculatorValorant.extensions.daysApart
import br.com.battlepassCalculatorValorant.model.GameType.DisputaDeSpike
import br.com.battlepassCalculatorValorant.model.GameType.GameType
import br.com.battlepassCalculatorValorant.model.GameType.SemClassificacao
import br.com.battlepassCalculatorValorant.model.Historic.Historic
import br.com.battlepassCalculatorValorant.model.Singleton.ManagerHistoric
import br.com.battlepassCalculatorValorant.model.Singleton.ManagerPassBattle
import br.com.battlepassCalculatorValorant.model.battlePass.Chapter
import br.com.battlepassCalculatorValorant.model.battlePass.Tier
import java.util.*
import java.util.stream.IntStream.range
import kotlin.collections.ArrayList

class Properties(val context: Context) {

    val historic: Historic = ManagerHistoric.getInstance(context)
    val passBattle = ManagerPassBattle.getInstance(context).getBattlePass()

    var semClassificacao: SemClassificacao = SemClassificacao()
    var disputaDeSpike: DisputaDeSpike = DisputaDeSpike()

    fun getExpectedExpPerDay(): ArrayList<Int> {
        val listOfAllExpExpectedPerDay = ArrayList<Int>()
        listOfAllExpExpectedPerDay.add(0)
        val totalExp = getTotalXpBattlePass()
        val daysDurationBattlePass = passBattle.dateFinally.daysApart(passBattle.dateInit)
        val razao = totalExp / daysDurationBattlePass
        for (day in range(1, daysDurationBattlePass)) {
            listOfAllExpExpectedPerDay.add(day * razao)
        }
        return listOfAllExpExpectedPerDay
    }

    fun getRealProgressPerDay(): ArrayList<Int> {
        val listOfAllExpExpectedPerDay = ArrayList<Int>()
        listOfAllExpExpectedPerDay.add(0)
        val daysDurationBattlePass = passBattle.dateFinally.daysApart(passBattle.dateInit)
        val razao = getXpPerDia()
        for (day in range(1, daysDurationBattlePass)) {
            listOfAllExpExpectedPerDay.add(day * razao)
        }
        return listOfAllExpExpectedPerDay
    }

    fun getTiersPerXp(): ArrayList<Int> {
        val progressoPerTier = ArrayList<Int>()
        progressoPerTier.add(0)
        progressoPerTier.addAll(passBattle.tiers.map { it.expInitial + it.expMissing })
        return progressoPerTier
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
                val tierCurrent =
                    passBattle.getTier(tierUserInput.tierCurrent) ?: passBattle.getTier(50)!!
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

    fun getListChapters(): ArrayList<Chapter> {
        return passBattle.chapters
    }

    fun getTierCurrent(): Int {
        val ultimoTier: Int = if (historic.isEmpty()) 0 else historic.last().tierCurrent
        return ultimoTier
    }

    fun getChapterCurrent(): Int {
        val tierCurrent: Int = getTierCurrent()
        return (tierCurrent - 1) / 5 + 1
    }

    fun getTotalXp(): Int {
        val xpPass =
            if (historic.isEmpty()) 0 else passBattle.getTier(historic.last().tierCurrent)!!.expInitial
        val xpCurrent =
            if (historic.isEmpty()) 0 else (passBattle.getTier(historic.last().tierCurrent)!!.expMissing - historic.last().tierExpMissing)
        return xpPass + xpCurrent
    }

    fun getTotalXpBattlePass(): Int {
        return if (passBattle.espilogoIsValide)
            passBattle.expTotal + passBattle.epilogoExpTotal
        else
            passBattle.expTotal
    }

    fun getProgressPorcent(): Double {
        val xpCurrent = getTotalXp()
        val xpTotal = getTotalXpBattlePass()
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
        val daysDurationBattlePass = passBattle.dateFinally.daysApart(passBattle.dateInit)
        val xpPerDay = (getTotalXpBattlePass().toDouble() / daysDurationBattlePass)
        val xpExpected = days * xpPerDay
        return getTotalXp() - xpExpected.toInt()
    }


    @SuppressLint("SimpleDateFormat")
    fun finishForecast(): String? {
        if (historic.isEmpty()) {
            return "00/00/0000"
        }
        val xpTotal = getTotalXpBattlePass().toDouble()
        val xpCurrent = getTotalXp().toDouble()
        val xpDif = xpTotal - xpCurrent
        val xpPerDayExpected = getXpPerDia()
        val totalDays = xpDif / xpPerDayExpected
        val dateFinally = Calendar.getInstance()
        dateFinally.add(Calendar.DAY_OF_MONTH, totalDays.toInt() + 1)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val dateFinallyFormated = sdf.format(dateFinally.time)
        return dateFinallyFormated
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
        val xpTotal = getTotalXpBattlePass()
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

    fun days(): Int {
        return passBattle.dateFinally.daysApart(passBattle.dateInit)
    }

    fun expMissaoDiaria(): Float {
        val days = days()
        val xpPerDay = passBattle.expMissaoDiaria.toFloat() / days
        val xpCurrent = xpPerDay * dayCurrent()
        return xpCurrent
    }

    fun expMissaoSemanal(): Float {
        val weeks = (days() / 7)
        val xpPerWeek = passBattle.expMissaoSemanal.toFloat() / weeks
        val xpCurrent = xpPerWeek * (dayCurrent() / 7)
        return xpCurrent
    }

    fun expNormalGame(): Int {
        val total = getTotalXp()
        return total - (getExpMissaoDiaria() + getExpMissaoSemanal())
    }

    fun getExpMissaoDiaria(): Int {
        return passBattle.expMissaoDiaria
    }

    fun getExpMissaoSemanal(): Int {
        return passBattle.expMissaoSemanal
    }

    fun getNormalGame(): Int {
        return (getTotalXpBattlePass() - (getExpMissaoSemanal() + getExpMissaoDiaria()))
    }

    fun getPercentMissaoDiaria(): Float {
        val missao = getExpMissaoDiaria()
        val total = getTotalXpBattlePass()
        return (missao.toFloat() / total.toFloat())
    }

    fun getPercentMissaoSemanal(): Float {
        val missao = getExpMissaoSemanal()
        val total = getTotalXpBattlePass()
        return (missao.toFloat() / total.toFloat()) + 0.01F
    }

    fun getPercentNormalGame(): Float {
        val missao = getNormalGame()
        val total = getTotalXpBattlePass()
        return (missao.toFloat() / total.toFloat()) + 0.01F
    }
}