package br.com.battlepassCalculatorValorant.ui.fragment.Propriedades

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.model.ColorFromXml
import br.com.battlepassCalculatorValorant.model.Observer.IObserver
import br.com.battlepassCalculatorValorant.model.Properties.Properties
import br.com.battlepassCalculatorValorant.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_progress.*

class ProgressFragment : Fragment(), IObserver {
    private lateinit var properties: Properties
    private lateinit var colorGenerator: ColorFromXml
    override fun onCreate(savedInstanceState: Bundle?) {

        properties = MainActivity.Companion.properties
        properties.historic.add(this)
        colorGenerator = MainActivity.Companion.colorXML
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        update()
    }

    override fun update() {
        val totalDeXp = properties.getTotalXp().toString() + " XP"
        val progressPorcent = properties.getProgressPorcent().toString() + "%"
        val xpPerDay = properties.getXpPerDia().toString() + " XP"
        val expAdiantadoAtrasado = properties.getExpAdiantAtrasado()
        val exp = "$expAdiantadoAtrasado XP"

        tag_exp_adiant_atrasado.hint =
            if (expAdiantadoAtrasado >= 0) "Exp Adiantado:" else "Exp Atrasado:"

        if (expAdiantadoAtrasado >= 0) {
            exp_adiant_atrasado.setTextColor(Color.parseColor(colorGenerator.getColor(R.attr.colorAccent)))
        } else {
            exp_adiant_atrasado.setTextColor(Color.parseColor(colorGenerator.getColor(R.attr.colorError)))
        }

        total_de_xp.text = totalDeXp
        progress_porcent.text = progressPorcent
        xp_p_dia.text = xpPerDay
        exp_adiant_atrasado.text = exp
    }
}