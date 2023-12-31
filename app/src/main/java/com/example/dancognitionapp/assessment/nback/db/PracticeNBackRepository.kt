package com.example.dancognitionapp.assessment.nback.db

import com.example.dancognitionapp.assessment.TrialDay
import com.example.dancognitionapp.assessment.TrialTime
import com.example.dancognitionapp.assessment.nback.data.NBackClickCategorization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class PracticeNBackRepository: NBackRepository {
    override suspend fun insertNBackItem(nBackItemEntity: NBackItemEntity) {
        /* DO NOTHING (no db actions for practice trials) */
    }

    override suspend fun updateReactionTimeAndClickDetailsForItem(
        reactionTime: Long,
        trialId: Int,
        listPosition: Int,
        blockNumber: Int,
        nValue: Int,
        clickCategorization: NBackClickCategorization,
        wasCorrectAction: Boolean
    ) {
        /* DO NOTHING (no db actions for practice trials) */
    }

    override suspend fun insertNBackTrial(nBackEntity: NBackEntity) {
        /* DO NOTHING (no db actions for practice trials) */
    }

    override suspend fun deleteNBackDataByParticipantId(participantId: Int) {
        /* DO NOTHING (no db actions for practice trials) */
    }

    override suspend fun deleteNBackDataByTrialId(trialId: Int) {
        /* DO NOTHING (no db actions for practice trials) */
    }

    override suspend fun getNBackEntityForTrial(
        participantId: Int,
        trialDay: TrialDay,
        trialTime: TrialTime
    ): NBackEntity? {
        /* DO NOTHING (no db actions for practice trials) */
        return null
    }

    override suspend fun getNBackTrialsByParticipantId(participantId: Int): Flow<List<NBackEntity>> {
        /* DO NOTHING (no db actions for practice trials) */
        return emptyFlow()
    }

    override suspend fun getNBackTrialsByTrialIds(trialIds: List<Int>): Flow<List<NBackTrialData>> {
        /* DO NOTHING (no db actions for practice trials) */
        return emptyFlow()
    }
}