package com.funsoftware.game.deliveryguy.leveleditor.event

data class GameWorldParamsChanged(
    val batchUnitsPerGameUnit: Float,
    val gameWorldBottomX: Float,
    val gameWorldBottomY: Float,
    val gameWorldWidth: Float,
    val gameWorldHeight: Float
)