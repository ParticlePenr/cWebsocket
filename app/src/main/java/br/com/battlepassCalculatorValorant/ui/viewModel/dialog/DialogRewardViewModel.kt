package br.com.battlepassCalculatorValorant.ui.viewModel.dialog

import androidx.lifecycle.ViewModel
import br.com.battlepassCalculatorValorant.model.battlePass.Reward
import br.com.battlepassCalculatorValorant.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialogRewardViewModel @Inject constructor(private val calculador: CalculatorRepository) :
    ViewModel() {
    fun getRewardById(id: Int): Reward = calculador.getRewardById(id)!!
}