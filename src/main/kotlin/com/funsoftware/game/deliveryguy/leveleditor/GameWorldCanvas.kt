package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.funsoftware.game.deliveryguy.leveleditor.event.GameWorldParamsChanged
import org.springframework.context.annotation.Lazy
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Lazy
class GameWorldCanvas : Stack() {

    private var batchUnitsPerGameUnit = 0f
    private var gameWorldLeftBottomX = 0f
    private var gameWorldLeftBottomY = 0f
    private var gameWorldWidth = 0f
    private var gameWorldHeight = 0f

    init {
        setFillParent(true)
    }

    @EventListener
    fun gameWorldParamsChanged(event: GameWorldParamsChanged) {
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