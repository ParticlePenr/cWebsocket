package br.com.samuelnunes.valorantpassbattle.ui.viewModel.fragment.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.samuelnunes.valorantpassbattle.model.dto.Reward
import br.com.samuelnunes.valorantpassbattle.model.dto.UserTier
import br.com.samuelnunes.valorantpassbattle.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChapterRewardsViewModel @Inject constructor(calculador: CalculatorRepository) :
    ViewModel() {

    val chapters: List<Reward> = calculador.listChapters
    val lastChapter: LiveData<UserTier> = calculador.lastUserInput.asLiveData()


}