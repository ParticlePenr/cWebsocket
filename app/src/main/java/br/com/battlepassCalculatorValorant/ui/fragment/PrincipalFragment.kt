package br.com.battlepassCalculatorValorant.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.model.Properties.Properties
import br.com.battlepassCalculatorValorant.ui.activity.MainActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class PrincipalFragment : Fragment() {
    private lateinit var properties: Properties

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        properties = MainActivity.properties
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        criaAnuncio(view, R.id.adView1)
        criaAnuncio(view, R.id.adView2)
    }

    fun criaAnuncio(view: View, id: Int) {
        val mAdView = view.findViewById<AdView>(id)
        val adRequest =
            AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        mAdView.loadAd(adRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        properties.historic.deleteAll()
    }
}