package com.funsoftware.game.deliveryguy.leveleditor.component

data class GameWorldGridLine(
    val lineX: Float, val lineY: Float,
    val lineWidth: Float, val lineHeight: Float,
    val firstLabelX: Float, val firstLabelY: Float,
    val secondLabelX: Float, val secondLabelY: Float,
    val labelText: String
)
