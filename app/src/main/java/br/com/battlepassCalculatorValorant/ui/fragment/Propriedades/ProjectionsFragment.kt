package br.com.battlepassCalculatorValorant.ui.fragment.Propriedades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.model.Observer.IObserver
import br.com.battlepassCalculatorValorant.model.Properties.Properties
import br.com.battlepassCalculatorValorant.model.SingletonPassBattle.ManagerProperties
import kotlinx.android.synthetic.main.fragment_projections.*

class ProjectionsFragment : Fragment(), IObserver {
    private lateinit var properties: Properties

    override fun onCreate(savedInstanceState: Bundle?) {
        properties = ManagerProperties.getInstance(requireContext())
        properties.historic.add(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_projections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        update()
    }

    override fun update() {

        val jogosRestantesSC = properties.jogosRestantes(properties.semClassificacao)
        val jogosRestantesDS = properties.jogosRestantes(properties.disputaDeSpike)
        val jogosRestantesGN = properties.jogosRestantes(properties.guerraDeBolaDeNeve)

        val tempoRestanteSC = properties.tempoRestante(properties.semClassificacao)
        val tempoRestanteDS = properties.tempoRestante(properties.disputaDeSpike)
        val tempoRestanteGN = properties.tempoRestante(properties.guerraDeBolaDeNeve)

        val jogosDiaSC = properties.jogosPorDia(properties.semClassificacao)
        val jogosDiaDS = properties.jogosPorDia(properties.disputaDeSpike)
        val jogosDiaGN = properties.jogosPorDia(properties.guerraDeBolaDeNeve)

        val horasDiasSC = properties.horasPorDia(properties.semClassificacao)
        val horasDiasDS = properties.horasPorDia(properties.disputaDeSpike)
        val horasDiasGN = properties.horasPorDia(properties.guerraDeBolaDeNeve)

        //Jogos Restantes
        tv_jogos_restantes_sc.text = jogosRestantesSC.toInt().toString()
        tv_jogos_restantes_ds.text = jogosRestantesDS.toInt().toString()
        tv_jogos_restantes_gn.text = jogosRestantesGN.toInt().toString()

        //Tempo Restante
        tv_tempo_restante_sc.text = convertHours(tempoRestanteSC)
        tv_tempo_restante_ds.text = convertHours(tempoRestanteDS)
        tv_tempo_restante_gn.text = convertHours(tempoRestanteGN)

        //Jogos Por Dia
        tv_jogos_dia_sc.text = jogosDiaSC.toString()
        tv_jogos_dia_ds.text = jogosDiaDS.toString()
        tv_jogos_dia_gn.text = jogosDiaGN.toString()

        //Horas por dia
        tv_horas_dia_sc.text = convertHours(horasDiasSC)
        tv_horas_dia_ds.text = convertHours(horasDiasDS)
        tv_horas_dia_gn.text = convertHours(horasDiasGN)
    }

    private fun convertHours(time: Float): String {
        val hours = time.toInt()
        val minutes = ((time % 1) * 60).toInt()
        return "${hours}:${minutes} Hrs"
    }

}
