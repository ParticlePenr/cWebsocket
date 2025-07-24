package br.com.samuelnunes.valorantpassbattle.ui.viewModel.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import br.com.samuelnunes.valorantpassbattle.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TierCurrentFragmentViewModel @Inject constructor(calculador: CalculatorRepository) :
    ViewModel() {
    val percentageTier: LiveData<Double> = calculador.percentageTier.asLiveData()
    val tierIndex: LiveData<Int> = calculador.tierCurrent.asLiveData().map { it.id }
    val tierName: LiveData<String> = calculador.tierCurrent.asLiveData().map { it.nome }
    val imagesURL: LiveData<List<String>> =
        calculador.tierCurrent.asLiveData().map { it.imagens }
}
