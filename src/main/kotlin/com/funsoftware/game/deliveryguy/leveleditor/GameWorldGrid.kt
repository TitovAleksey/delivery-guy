package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.DragListener

class GameWorldGrid(skin: Skin) : Widget() {

    private var firstWorldY = 0
    private var lastWorldY = 0
    private var firstWorldX = 0
    private var lastWorldX = 0

    private var gameWorldBottomX = 0f
    private var gameWorldBottomY = 0f

    private var horizontalPaddingWidth = 0f
    private var verticalPaddingHeight = 0f

    private val batchUnitsPerCm = Gdx.graphics.density * 160f / 2.54f
    private val textPaddingSize = batchUnitsPerCm / 4
    private var gameUnitsPerCell = 1
    private var batchUnitsPerGameUnit = 1f
    private val worldLinesTexture = createOnePixelTexture()
    private val bitmapFontCache: BitmapFontCache
    private val layout: GlyphLayout = GlyphLayout()

    init {
        setFillParent(true)
        createDragListener()
        bitmapFontCache = createBitMapFontCache(skin)
        layout.setText(bitmapFontCache.font, "1")
        bitmapFontCache.font.data.setScale(batchUnitsPerCm / 2.5f / layout.height)
    }

    private fun createDragListener() {
        val dragListener = object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                gameWorldBottomX -= deltaX / batchUnitsPerGameUnit
                gameWorldBottomY -= deltaY / batchUnitsPerGameUnit
                invalidate()
            }
        }
        dragListener.button = Input.Buttons.RIGHT
        dragListener.tapSquareSize
        addListener(dragListener)
    }

    private fun createOnePixelTexture(): Texture {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.DARK_GRAY)
        pixmap.fillRectangle(0, 0, 1, 1)
        val texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }

    private fun createBitMapFontCache(skin: Skin): BitmapFontCache {
        return skin.get(Label.LabelStyle::class.java).font.newFontCache()
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
        val yInBatchCoordinates = y + (gameWorldLineY - gameWorldBottomY) * batchUnitsPerGameUnit
        batch.draw(
            worldLinesTexture,
            x + horizontalPaddingWidth,
            yInBatchCoordinates,
            width - horizontalPaddingWidth * 2,
            1f
        )
        layout.setText(bitmapFontCache.font, gameWorldLineY.toString())
        val yInBatchCoordinatesForText = yInBatchCoordinates + layout.height / 2
        bitmapFontCache.setText(layout, x + horizontalPaddingWidth / 2 - layout.width / 2, yInBatchCoordinatesForText)
        bitmapFontCache.draw(batch)
        bitmapFontCache.setText(
            layout,
            x + width - horizontalPaddingWidth / 2 - layout.width / 2,
            yInBatchCoordinatesForText
        )
        bitmapFontCache.draw(batch)
    }

    private fun drawVerticalWorldLineWithLabel(gameWorldLineX: Int, batch: Batch) {
        val xInBatchCoordinates = x + (gameWorldLineX - gameWorldBottomX) * batchUnitsPerGameUnit
        batch.draw(
            worldLinesTexture,
            xInBatchCoordinates,
            y + verticalPaddingHeight,
            1f,
            height - verticalPaddingHeight * 2
        )
        layout.setText(bitmapFontCache.font, gameWorldLineX.toString())
        val xInBatchCoordinatesForText = xInBatchCoordinates - layout.width / 2
        bitmapFontCache.setText(layout, xInBatchCoordinatesForText, y + verticalPaddingHeight / 2 + layout.height / 2)
        bitmapFontCache.draw(batch)
        bitmapFontCache.setText(
            layout,
            xInBatchCoordinatesForText,
            y + height - verticalPaddingHeight / 2 + layout.height / 2
        )
        bitmapFontCache.draw(batch)
    }

    override fun layout() {
        layout.setText(bitmapFontCache.font, "1")
        if (layout.height > batchUnitsPerGameUnit * gameUnitsPerCell) {
            batchUnitsPerGameUnit = layout.height / gameUnitsPerCell * 2f
        }

        val (firstX, lastX) = calculateGameWorldXValues()
        val (firstY, lastY) = calculateGameWorldYValues()
        val (maxYLabelWidth, _) = calculateMaxLabelSize(firstY, lastY)
        val (_, maxXLabelHeight) = calculateMaxLabelSize(firstX, lastX)
        horizontalPaddingWidth = maxYLabelWidth + textPaddingSize * 2
        verticalPaddingHeight = maxXLabelHeight + textPaddingSize * 2

        firstWorldY = firstY
        while (isHorizontalWorldLineBeyondPadding(firstWorldY)) firstWorldY += gameUnitsPerCell
        lastWorldY = lastY
        while (isHorizontalWorldLineBeyondPadding(lastWorldY)) lastWorldY -= gameUnitsPerCell

        firstWorldX = firstX
        while (isVerticalWorldLineBeyondPadding(firstWorldX)) firstWorldX += gameUnitsPerCell
        lastWorldX = lastX
        while (isVerticalWorldLineBeyondPadding(lastWorldX)) lastWorldX -= gameUnitsPerCell
    }

    private fun calculateGameWorldXValues(): List<Int> {
        val gameWorldWidth = width / batchUnitsPerGameUnit
        val firstWorldXLine = (gameWorldBottomX - gameWorldBottomX % gameUnitsPerCell).toInt()
        val lastWorldXLine = firstWorldXLine + (gameWorldWidth - gameWorldWidth % gameUnitsPerCell).toInt()
        return listOf(firstWorldXLine, lastWorldXLine)
    }

    private fun calculateGameWorldYValues(): List<Int> {
        val gameWorldHeight = height / batchUnitsPerGameUnit
        val firstWorldYLine = (gameWorldBottomY - gameWorldBottomY % gameUnitsPerCell).toInt()
        val lastWorldYLine = firstWorldYLine + (gameWorldHeight - gameWorldHeight % gameUnitsPerCell).toInt()
        return listOf(firstWorldYLine, lastWorldYLine)
    }

    private fun calculateMaxLabelSize(firstLabelValue: Int, lastLabelValue: Int): List<Float> {
        var maxLabelWidth = 0f
        var maxLabelHeight = 0f
        for (libelValue in firstLabelValue..lastLabelValue step gameUnitsPerCell) {
            layout.setText(bitmapFontCache.font, libelValue.toString())
            maxLabelWidth = if (layout.width > maxLabelWidth) layout.width else maxLabelWidth
            maxLabelHeight = if (layout.height > maxLabelHeight) layout.height else maxLabelHeight
        }
        return listOf(maxLabelWidth, maxLabelHeight)
    }

    private fun isHorizontalWorldLineBeyondPadding(worldLineValue: Int): Boolean {
        val yInBatchCoordinates = y + (worldLineValue - gameWorldBottomY) * batchUnitsPerGameUnit
        layout.setText(bitmapFontCache.font, worldLineValue.toString())
        val textLabelBatchBottom = yInBatchCoordinates - layout.height / 2
        val textLabelBatchTop = yInBatchCoordinates + layout.height / 2
        return textLabelBatchBottom < y + verticalPaddingHeight || textLabelBatchTop > y + height - verticalPaddingHeight
    }

    private fun isVerticalWorldLineBeyondPadding(worldLineValue: Int): Boolean {
        val xInBatchCoordinates = x + (worldLineValue - gameWorldBottomX) * batchUnitsPerGameUnit
        layout.setText(bitmapFontCache.font, worldLineValue.toString())
        val textLabelBatchLeft = xInBatchCoordinates - layout.width / 2
        val textLabelBatchRight = xInBatchCoordinates + layout.width / 2
        return textLabelBatchLeft < x + horizontalPaddingWidth || textLabelBatchRight > x + width - horizontalPaddingWidth
    }
}