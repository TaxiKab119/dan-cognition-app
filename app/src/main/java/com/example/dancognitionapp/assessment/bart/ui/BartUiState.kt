package com.example.dancognitionapp.assessment.bart.ui

import com.example.dancognitionapp.assessment.bart.data.Balloon
import java.util.LinkedList

data class BartUiState(
    val balloonList: LinkedList<Balloon>,
    val currentBalloon: Balloon = balloonList.pop(),
    val currentInflationCount: Int = 0,
    val currentReward: Int = 1,
    val totalEarnings: Int = 0,
    val isBalloonPopped: Boolean = false,
    val isTestComplete: Boolean = false,
    val hasTestBegun: Boolean = false
)
