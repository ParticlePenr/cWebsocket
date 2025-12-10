package br.com.samuelnunes.valorantpassbattle.model

import android.content.Context
import br.com.samuelnunes.valorantpassbattle.R
import br.com.samuelnunes.valorantpassbattle.model.dto.BattlePass
import br.com.samuelnunes.valorantpassbattle.model.dto.Reward
import br.com.samuelnunes.valorantpassbattle.util.ObjectConverters
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BattlePassManager(context: Context) {
    private val jsonStr: String =
        context.resources.openRawResource(R.raw.passe).bufferedReader().use { it.readText() }

    private val passe: BattlePass =
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, ObjectConverters.LD_DESERIALIZER)
            .create().fromJson(jsonStr, BattlePass::class.java)

    private val today: LocalDate = LocalDate.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val passDurationInDays: Int =
        ChronoUnit.DAYS.between(passe.dateInit, passe.dateFinally).toInt() + 1
    val daysFromTheStart: Int = ChronoUnit.DAYS.between(passe.dateInit, today).toInt()
    val daysLeftUntilTheEnd: Int = ChronoUnit.DAYS.between(today, passe.dateFinally).toInt() + 1

    val openingDateOfTheAct: String = passe.dateInit.format(formatter)
    val closingDateOfTheAct: String = passe.dateFinally.format(formatter)

    val dateFinally = passe.dateFinally

    fun getExpMissaoDiaria(days: Int): Int {
        return if (days <= passDurationInDays) {
            days * passe.missaoDiaria.exp
        } else {
            days * passDurationInDays
        }
    }

    fun getExpMissaoSemanal(days: Int): Int {
        val semanaAtual = (days / 7) + 1
        val tiers = passe.missaoSemanal.filter { it.id <= semanaAtual }
        return tiers.map { it.exp }.sum()
    }

    fun getIdBattlePass(): String {
        return passe.id
    }

    fun getTier(id: Int): Reward? {
        return passe.tiers.find { it.id == id }
    }

    fun getTiers(): List<Reward> = passe.tiers

    fun getChapter(id: Int): Reward? {
        return passe.capitulos.find { it.id == id }
    }

    fun getChapters(): List<Reward> = passe.capitulos

    fun expParaCompletarTier(n: Int): Int {
        return when (n) {
            in 2..56 -> {
                2000 + (n - 2) * 750
            }
            else -> {
                0
            }
        }
    }

    fun totalExpAteOTier(tierMax: Int): Int {
        var aux = 0
        for (tier in 1..tierMax) {
            aux += expParaCompletarTier(tier)
        }
        return aux
    }

}