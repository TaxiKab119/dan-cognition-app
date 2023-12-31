package com.example.dancognitionapp.assessment.bart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dancognitionapp.assessment.TrialDay
import com.example.dancognitionapp.assessment.TrialTime
import com.example.dancognitionapp.assessment.bart.data.Balloon
import com.example.dancognitionapp.assessment.bart.data.BalloonGenerator
import com.example.dancognitionapp.assessment.bart.db.BalloonEntity
import com.example.dancognitionapp.assessment.bart.db.BartEntity
import com.example.dancognitionapp.assessment.bart.db.BartRepository
import com.example.dancognitionapp.participants.db.Participant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class BartViewModel(private val bartRepository: BartRepository): ViewModel() {

    private val balloonList = BalloonGenerator().balloons
    private val firstBalloon: Balloon = balloonList.poll() ?: balloonList.first

    /**
     * This first block sets up the ui state to be mutable and initializes the BalloonList
     * */
    private val _uiState = MutableStateFlow(
        BartUiState(
            balloonList = balloonList,
            currentBalloon = firstBalloon
        )
    )
    val uiState: StateFlow<BartUiState> = _uiState.asStateFlow()
    private val currentState: BartUiState
        get() = _uiState.value

    private lateinit var currentBartEntity: BartEntity
    fun initBart(participant: Participant, trialDay: TrialDay, trialTime: TrialTime) {
        viewModelScope.launch(Dispatchers.IO) {
            currentBartEntity = BartEntity(
                participantId = participant.id,
                trialTime = trialTime,
                trialDay = trialDay,
                userGivenParticipantId = participant.userGivenId
            )
            async {
                bartRepository.insertBart(currentBartEntity)
            }.await()
            async {
                currentBartEntity = currentBartEntity.copy(
                    id = bartRepository.getBartEntityByParticipantTrialData(
                        participantId = participant.id,
                        trialDay = trialDay,
                        trialTime = trialTime
                    )?.id ?: 0
                )
            }.await()
            bartRepository.insertBalloon(
                _uiState.value.toBalloonEntity(currentBartEntity.id)
            )
        }
    }

    fun hideInstructions() {
        _uiState.update {
            it.copy(
                hasTestBegun = true
            )
        }
    }

    fun inflateBalloon() {
        val canInflate =
            currentState.currentBalloon.maxInflations > currentState.currentInflationCount

        if (canInflate) {
            _uiState.value = currentState.copy(
                currentInflationCount = currentState.currentInflationCount.inc(),
                currentReward = currentState.currentReward.inc(),
                isBalloonPopped = false
            )
            Timber.i("Balloon Number ${currentState.currentBalloon.listPosition} was inflated!")
            Timber.d("\nInflations: ${currentState.currentInflationCount}" +
                    "\nmaxInflationCount: ${currentState.currentBalloon.maxInflations}" +
                    "\nCurrent Reward: ${currentState.currentReward}")

            viewModelScope.updateBalloon(currentState.currentInflationCount, false)
        } else {
            // Update balloon inflations before it gets reset to 0
            viewModelScope.updateBalloon(currentState.currentInflationCount + 1, true)

            Timber.i(
                "Balloon Number ${currentState.currentBalloon.listPosition} popped!" +
                        " Max Inflation: ${currentState.currentBalloon.maxInflations} " +
                        " == Number of User Clicks ${currentState.currentInflationCount}"
            )
            _uiState.value = currentState.copy(
                currentInflationCount = 0,
                currentReward = 1,
                isBalloonPopped = true
            )
            toNextBalloon()
        }
    }

    fun resetBalloonStatus() {
        _uiState.value = currentState.copy(
            isBalloonPopped = false
        )
    }

    fun hideDialog() {
        _uiState.value = currentState.copy(
            isBalloonPopped = false,
            isTestComplete = false
        )
    }

    fun collectBalloonReward() {
        _uiState.value = currentState.copy(
            totalEarnings = currentState.totalEarnings + currentState.currentReward,
            currentReward = 1,
            currentInflationCount = 0
        )
        Timber.i("Balloon Number ${currentState.currentBalloon.listPosition} collected!")
        toNextBalloon()
    }
    private fun CoroutineScope.updateBalloon(newInflations: Int, didPop: Boolean) {
        this.launch(Dispatchers.IO) {
            bartRepository.updateBalloonInflations(
                bartId = currentBartEntity.id,
                newInflationCount = newInflations,
                listPosition = currentState.currentBalloon.listPosition,
                didPop = didPop
            )
        }
    }

    private fun toNextBalloon() {
        if (balloonList.isEmpty()) {
            _uiState.value = currentState.copy(
                isTestComplete = true
            )
            return
        }
        val nextBalloon = balloonList.pop()
        _uiState.value = currentState.copy(
            balloonList = balloonList,
            currentBalloon = nextBalloon
        )
        viewModelScope.launch(Dispatchers.IO) {
            bartRepository.insertBalloon(
                BalloonEntity(
                    balloonNumber = nextBalloon.listPosition,
                    maxInflations = nextBalloon.maxInflations,
                    numberOfInflations = 0,
                    didPop = false,
                    bartEntityId = currentBartEntity.id
                )
            )
        }
    }
}

private fun BartUiState.toBalloonEntity(bartEntityId: Int): BalloonEntity = BalloonEntity(
    balloonNumber = currentBalloon.listPosition,
    maxInflations = currentBalloon.maxInflations,
    numberOfInflations = currentInflationCount,
    didPop = isBalloonPopped,
    bartEntityId = bartEntityId
)