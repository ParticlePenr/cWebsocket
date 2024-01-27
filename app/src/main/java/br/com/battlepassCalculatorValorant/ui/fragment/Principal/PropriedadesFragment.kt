package br.com.battlepassCalculatorValorant.ui.fragment.Principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import br.com.battlepassCalculatorValorant.R
import br.com.battlepassCalculatorValorant.ui.adapter.MyPageSliderAdapter
import br.com.battlepassCalculatorValorant.ui.fragment.Propriedades.ProgressFragment
import br.com.battlepassCalculatorValorant.ui.fragment.Propriedades.ProjectionsFragment
import br.com.battlepassCalculatorValorant.ui.fragment.Propriedades.TimelineFragment
import kotlinx.android.synthetic.main.fragment_propriedades.*


class PropriedadesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_propriedades, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MyPageSliderAdapter(this)

        adapter.addFragment(ProgressFragment())
        adapter.addFragment(TimelineFragment())
        adapter.addFragment(ProjectionsFragment())
        pager.adapter = adapter
        dots_indicator.setViewPager2(pager)
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val v: View = adapter.getItem(position)?.requireView()!!
                v.post {
                    val wMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(v.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec =
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    v.measure(wMeasureSpec, hMeasureSpec)

                    if (pager.layoutParams.height != v.measuredHeight) {
                        pager.layoutParams = (pager.layoutParams as ViewGroup.LayoutParams)
                            .also { lp -> lp.height = v.measuredHeight }
                    }
                }
            }
        })
    }
}

