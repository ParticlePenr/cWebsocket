package br.com.battlepassCalculatorValorant.ui.fragment.Propriedades

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.model.ColorFromXml
import br.com.battlepassCalculatorValorant.model.Observer.IObserver
import br.com.battlepassCalculatorValorant.model.Properties.Properties
import br.com.battlepassCalculatorValorant.ui.activity.MainActivity
import br.com.battlepassCalculatorValorant.ui.progressBar.mProgressBarView
import kotlinx.android.synthetic.main.dialog_title.view.*


class ExpStatisticFragment : Fragment(), IObserver {

    private lateinit var properties: Properties
    private lateinit var jogosNormais: mProgressBarView
    private lateinit var missoesDiarias: mProgressBarView
    private lateinit var missoesSemanais: mProgressBarView
    private lateinit var btnInfo: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        properties = MainActivity.properties
        properties.historic.add(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exp_statistic, container, false)
        setupViews(view)
        setOnClickListeners()
        update()
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setOnClickListeners() {
        btnInfo.setOnClickListener {
            val dialog = AlertDialog
                .Builder(context, R.style.alertDialog)
                .setCustomTitle(createTitle("Info EXP de Missoes"))
                .setMessage(
                    "Este gráfico mostra a quantidade de EXP atual, " +
                            "adquirida por meio das recompensas por partida, " +
                            "missões diárias e semanais!! Quanto mais missões realizadas, " +
                            "menor a quantidade de EXP por partida será " +
                            "necessária para completar o passe!"
                )
            dialog.show()
        }
    }

    private fun createTitle(title: String): View {
        val titleView: View = this.layoutInflater.inflate(R.layout.dialog_title, null)
        titleView.title.text = title
        return titleView
    }

    private fun setupViews(v: View) {
        jogosNormais = v.findViewById(R.id.pb_jogos_normais)
        missoesDiarias = v.findViewById(R.id.pb_missoes_diarias)
        missoesSemanais = v.findViewById(R.id.pb_missoes_semanais)
        btnInfo = v.findViewById(R.id.img_btn_info)
    }

    private fun getColor(color: Int): Int {
        return Color.parseColor(ColorFromXml(requireContext()).getColor(color))
    }

    override fun update() {
        jogosNormais.setupProgress(
            "EXP de Partidas",
            properties.expNormalGame(),
            properties.getNormalGame(),
            properties.getPercentNormalGame(),
            getColor(R.attr.colorSecondary)
        )

        missoesDiarias.setupProgress(
            "Missões Diárias",
            properties.expMissaoDiaria().toInt(),
            properties.getExpMissaoDiaria(),
            properties.getPercentMissaoDiaria(),
            getColor(R.attr.colorAccent)
        )
        missoesSemanais.setupProgress(
            "Missões Semanais",
            properties.expMissaoSemanal().toInt(),
            properties.getExpMissaoSemanal(),
            properties.getPercentMissaoSemanal(),
            getColor(R.attr.colorError)
        )
    }
}