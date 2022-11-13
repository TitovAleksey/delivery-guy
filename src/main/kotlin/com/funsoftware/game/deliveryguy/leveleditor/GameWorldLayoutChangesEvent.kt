package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.scenes.scene2d.Event

class GameWorldLayoutChangesEvent(
    val batchUnitsPerGameUnit: Float,
    val gameWorldBottomX: Float,
    val gameWorldBottomY: Float,
    val gameWorldWidth: Float,
    val gameWorldHeight: Float
) : Event()