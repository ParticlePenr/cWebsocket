package br.com.battlepassCalculatorValorant.ui.viewModel.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.battlepassCalculatorValorant.database.room.model.UserTier
import br.com.battlepassCalculatorValorant.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditHistoricActivityViewModel @Inject constructor(val calculador: CalculatorRepository) :
    ViewModel() {

    val allUserInput: LiveData<List<UserTier>> = calculador.allUserInput.asLiveData()

}