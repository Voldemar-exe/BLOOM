package com.example.data.models

import java.util.UUID

data class LeaderBoardUser(
    val leaderBoardRecordId: UUID,
    val userId: UUID,
    val nickname: String, // TODO Is optional? Check it.
    val score: Double,
    val rank: String, // TODO Add Data Class for Ranking with special icon, color etc.
)
