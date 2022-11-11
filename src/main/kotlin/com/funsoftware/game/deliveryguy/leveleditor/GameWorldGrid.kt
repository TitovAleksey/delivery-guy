package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.DragListener

class GameWorldGrid : Widget() {

    private var firstWorldY = 0
    private var lastWorldY = 0
    private var firstWorldX = 0
    private var lastWorldX = 0

    private var gameWorldBottomX = 0f
    private var gameWorldBottomY = 0f

    private var gameUnitsPerCell = 1
    private var pixelWidthInGameUnits = 0f
    private var pixelHeightInGameUnits = 0f
    private val worldLinesTexture = createOnePixelTexture()
    private val dragListener = createDragListener()

    private fun createOnePixelTexture(): Texture {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.GREEN)
        pixmap.fillRectangle(0, 0, 1, 1)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }

    private fun createDragListener(): DragListener {
        val dragListener = object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                gameWorldBottomX -= deltaX
                gameWorldBottomY -= deltaY
                invalidate()
            }
        }
        dragListener.button = Input.Buttons.RIGHT
        addListener(dragListener)
        return dragListener
    }

    override fun setStage(stage: Stage?) {
        super.setStage(stage)
        if (stage != null) {
            pixelWidthInGameUnits = stage.viewport.worldWidth / stage.viewport.screenWidth
            pixelHeightInGameUnits = stage.viewport.worldHeight / stage.viewport.screenHeight
            dragListener.tapSquareSize = pixelWidthInGameUnits.coerceAtMost(pixelWidthInGameUnits)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawSizeLineWithLabels(batch)
    }

    private fun drawSizeLineWithLabels(batch: Batch) {
        for (gameWorldLineY in firstWorldY..lastWorldY step gameUnitsPerCell) {
            drawHorizontalWorldLineWithLabel(gameWorldLineY, batch)
        }
        for (gameWorldLineX in firstWorldX..lastWorldX step gameUnitsPerCell) {
            drawVerticalWorldLineWithLabel(gameWorldLineX, batch)
        }
    }

    private fun drawHorizontalWorldLineWithLabel(gameWorldLineY: Int, batch: Batch) {
        batch.draw(
            worldLinesTexture,
            x,
            y + gameWorldLineY.toFloat() - gameWorldBottomY,
            width,
            pixelHeightInGameUnits
        )
    }

    private fun drawVerticalWorldLineWithLabel(gameWorldLineX: Int, batch: Batch) {
        batch.draw(
            worldLinesTexture,
            x + gameWorldLineX.toFloat() - gameWorldBottomX,
            y,
            pixelWidthInGameUnits,
            height
        )
    }

    override fun layout() {
        val (firstX, lastX) = calculateGameWorldXValues()
        firstWorldX = firstX
        lastWorldX = lastX
        val (firstY, lastY) = calculateGameWorldYValues()
        firstWorldY = firstY
        lastWorldY = lastY
    }

    private fun calculateGameWorldXValues(): List<Int> {
        val firstWorldXLine = (gameWorldBottomX - gameWorldBottomX % gameUnitsPerCell).toInt()
        val lastWorldXLine = firstWorldXLine + (width - width % gameUnitsPerCell).toInt()
        return listOf(firstWorldXLine, lastWorldXLine)
    }

    private fun calculateGameWorldYValues(): List<Int> {
        val firstWorldYLine = (gameWorldBottomY - gameWorldBottomY % gameUnitsPerCell).toInt()
        val lastWorldYLine = firstWorldYLine + (height - height % gameUnitsPerCell).toInt()
        return listOf(firstWorldYLine, lastWorldYLine)
    }
}