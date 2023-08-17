package com.example.dancognitionapp.participants

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dancognitionapp.participants.data.ParticipantRepository
import com.example.dancognitionapp.participants.data.ParticipantUiState

class ParticipantsViewModel(
    private val dataSource: ParticipantRepository = ParticipantRepository
): ViewModel() {
    /**
     * This first block sets up the ui state to be mutable initializes participant list
     * */
    private val _uiState = mutableStateOf(
        ParticipantUiState(participantModelList = dataSource.participantModelLists)
    )
    val uiState: State<ParticipantUiState> = _uiState

    private val currentState: ParticipantUiState
        get() = _uiState.value
}