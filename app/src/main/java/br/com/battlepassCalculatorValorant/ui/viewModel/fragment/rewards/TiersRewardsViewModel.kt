package br.com.battlepassCalculatorValorant.ui.viewModel.fragment.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.battlepassCalculatorValorant.database.room.model.UserTier
import br.com.battlepassCalculatorValorant.model.battlePass.Reward
import br.com.battlepassCalculatorValorant.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TiersRewardsViewModel @Inject constructor(calculador: CalculatorRepository) :
    ViewModel() {

    val tiers: List<Reward> = calculador.listTiers
    val lastTier: LiveData<UserTier> = calculador.lastUserInput.asLiveData()


}