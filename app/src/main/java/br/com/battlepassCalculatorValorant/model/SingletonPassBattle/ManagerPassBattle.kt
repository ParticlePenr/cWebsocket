package br.com.battlepassCalculatorValorant.model.SingletonPassBattle

import android.content.Context
import br.com.battlepassCalculatorValorant.model.BattlePass.BattlePassFactory

class ManagerPassBattle {
    companion object : SingletonHolder<BattlePassFactory, Context>(::BattlePassFactory)
}

