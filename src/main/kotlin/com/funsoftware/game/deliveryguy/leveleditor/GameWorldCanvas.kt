package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameWorldCanvas : WidgetGroup() {

    private var batchUnitsPerGameUnit = 0f
    private var gameWorldLeftBottomX = 0f
    private var gameWorldLeftBottomY = 0f
    private var gameWorldWidth = 0f
    private var gameWorldHeight = 0f

    init {
        var isProcessed = false
        addListener {
            if (it is GameWorldLayoutChangesEvent) {
                handleGameWorldChanges(it)
                isProcessed = true
            }
            isProcessed
        }
    }

    private fun handleGameWorldChanges(event: GameWorldLayoutChangesEvent) {
        batchUnitsPerGameUnit = event.batchUnitsPerGameUnit
        gameWorldLeftBottomX = event.gameWorldBottomX
        gameWorldLeftBottomY = event.gameWorldBottomY
        gameWorldWidth = event.gameWorldWidth
        gameWorldHeight = event.gameWorldHeight
        invalidate()
    }

    override fun layout() {
        (stage.viewport as ScreenViewport).unitsPerPixel = 1 / batchUnitsPerGameUnit
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height)
        stage.camera.position.x = gameWorldLeftBottomX + gameWorldWidth / 2
        stage.camera.position.y = gameWorldLeftBottomY + gameWorldHeight / 2
        stage.camera.update()
    }
}