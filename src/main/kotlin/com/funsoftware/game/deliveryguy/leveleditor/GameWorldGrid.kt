package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFontCache
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.utils.Pools
import com.funsoftware.game.deliveryguy.leveleditor.event.GameWorldParamsChanged
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
@Lazy
class GameWorldGrid(private val eventPublisher: ApplicationEventPublisher, skin: Skin) : Widget() {

    private var isFirstLayout = true
    private var gameUnitsPerCell = 1
    private var batchUnitsPerGameUnit = 1f

    private var mousePositionBatchX = 0f
    private var mousePositionBatchY = 0f
    private var mousePositionGameWorldX = 0f
    private var mousePositionGameWorldY = 0f

    private var gameWorldBottomX = 0f
    private var gameWorldBottomY = 0f

    private val batchUnitsPerCm = Gdx.graphics.density * 160f / 2.54f
    private val textPaddingSize = batchUnitsPerCm / 4

    private val worldLinesTexture = createOnePixelTexture()
    private val bitmapFontCache: BitmapFontCache
    private val layout: GlyphLayout = GlyphLayout()
    private val linesList: MutableList<GameWorldGridLine> = mutableListOf()
    private val eventsPool = Pools.get(GameWorldParamsChanged::class.java)

    init {
        setFillParent(true)
        createDragListener()
        createScrollListener()
        createMouseListener()
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

    private fun createScrollListener() {
        val scrollListener = object : InputListener() {
            override fun scrolled(event: InputEvent?, x: Float, y: Float, amountX: Float, amountY: Float): Boolean {
                if (amountY < 0) zoomInGameWorld()
                if (amountY > 0) zoomOutGameWorld()
                return true
            }

            override fun keyTyped(event: InputEvent?, character: Char): Boolean {
                var isProcessed = true
                when (character) {
                    '+' -> zoomInGameWorld()
                    '-' -> zoomOutGameWorld()
                    else -> {
                        isProcessed = false
                    }
                }
                return isProcessed
            }
        }
        addListener(scrollListener)
    }

    private fun zoomInGameWorld() {
        batchUnitsPerGameUnit *= 1.05f
        makeAlignOffset()
        invalidate()
    }

    private fun makeAlignOffset() {
        val mouseWorldXPositionAfterZoom = gameWorldBottomX + (mousePositionBatchX - x) / batchUnitsPerGameUnit
        val mouseWorldYPositionAfterZoom = gameWorldBottomY + (mousePositionBatchY - y) / batchUnitsPerGameUnit
        gameWorldBottomX -= mouseWorldXPositionAfterZoom - mousePositionGameWorldX
        gameWorldBottomY -= mouseWorldYPositionAfterZoom - mousePositionGameWorldY
    }

    private fun zoomOutGameWorld() {
        batchUnitsPerGameUnit *= 0.95f
        makeAlignOffset()
        invalidate()
    }

    private fun createMouseListener() {
        val mouseListener = object : InputListener() {
            override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                mousePositionGameWorldX = gameWorldBottomX + (x - this@GameWorldGrid.x) / batchUnitsPerGameUnit
                mousePositionGameWorldY = gameWorldBottomY + (y - this@GameWorldGrid.y) / batchUnitsPerGameUnit
                mousePositionBatchX = x
                mousePositionBatchY = y
                return true
            }

            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                stage?.scrollFocus = this@GameWorldGrid
                stage?.keyboardFocus = this@GameWorldGrid
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                if (toActor == this@GameWorldGrid) return
                stage?.scrollFocus = null
                stage?.keyboardFocus = null
            }
        }
        addListener(mouseListener)
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

    override fun hit(x: Float, y: Float, touchable: Boolean): Actor {
        return this
    }

    private fun drawSizeLineWithLabels(batch: Batch) {
        linesList.forEach {
            batch.draw(worldLinesTexture, it.lineX, it.lineY, it.lineWidth, it.lineHeight)
            layout.setText(bitmapFontCache.font, it.labelText)
            bitmapFontCache.setText(layout, it.firstLabelX, it.firstLabelY)
            bitmapFontCache.draw(batch)
            bitmapFontCache.setText(layout, it.secondLabelX, it.secondLabelY)
            bitmapFontCache.draw(batch)
        }
    }

    override fun layout() {
        var continueWork = true
        var tryToDecreaseStep = true
        while (continueWork) {
            val (firstX, lastX) = calculateGameWorldXValues()
            val (firstY, lastY) = calculateGameWorldYValues()
            val (maxYLabelWidth, maxYLabelHeight) = calculateMaxLabelSize(firstY, lastY)
            val (maxXLabelWidth, maxXLabelHeight) = calculateMaxLabelSize(firstX, lastX)
            val horizontalPaddingWidth = maxYLabelWidth + textPaddingSize * 2
            val verticalPaddingHeight = maxXLabelHeight + textPaddingSize * 2

            val cellSize = batchUnitsPerGameUnit * gameUnitsPerCell
            val minCellSize = maxXLabelWidth.coerceAtLeast(maxYLabelHeight) + textPaddingSize
            if (cellSize < minCellSize) {
                if (isFirstLayout) {
                    batchUnitsPerGameUnit = minCellSize / gameUnitsPerCell * 1.001f
                    isFirstLayout = false
                } else {
                    increaseGameWorldStep()
                    tryToDecreaseStep = false
                }
                continue
            } else {
                if (gameUnitsPerCell > 1 && tryToDecreaseStep) {
                    decreaseGameWorldStep()
                    continue
                }
            }
            continueWork = false

            linesList.clear()
            for (gameWorldLineX in firstX..lastX step gameUnitsPerCell) {
                if (!isVerticalWorldLineBeyondPadding(gameWorldLineX, horizontalPaddingWidth)) {
                    val xInBatchCoordinates = x + (gameWorldLineX - gameWorldBottomX) * batchUnitsPerGameUnit
                    val xInBatchCoordinatesForText = xInBatchCoordinates - layout.width / 2
                    linesList.add(
                        GameWorldGridLine(
                            xInBatchCoordinates, y + verticalPaddingHeight,
                            1f, height - verticalPaddingHeight * 2,
                            xInBatchCoordinatesForText, y + verticalPaddingHeight / 2 + layout.height / 2,
                            xInBatchCoordinatesForText, y + height - verticalPaddingHeight / 2 + layout.height / 2,
                            gameWorldLineX.toString()
                        )
                    )
                }
            }
            for (gameWorldLineY in firstY..lastY step gameUnitsPerCell) {
                if (!isHorizontalWorldLineBeyondPadding(gameWorldLineY, verticalPaddingHeight)) {
                    val yInBatchCoordinates = y + (gameWorldLineY - gameWorldBottomY) * batchUnitsPerGameUnit
                    val yInBatchCoordinatesForText = yInBatchCoordinates + layout.height / 2
                    linesList.add(
                        GameWorldGridLine(
                            x + horizontalPaddingWidth, yInBatchCoordinates,
                            width - horizontalPaddingWidth * 2, 1f,
                            x + horizontalPaddingWidth / 2 - layout.width / 2, yInBatchCoordinatesForText,
                            x + width - horizontalPaddingWidth / 2 - layout.width / 2, yInBatchCoordinatesForText,
                            gameWorldLineY.toString()
                        )
                    )
                }
            }
        }
        publishEvent()
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

    private fun increaseGameWorldStep() {
        gameUnitsPerCell *= 10
    }

    private fun decreaseGameWorldStep() {
        gameUnitsPerCell /= 10
    }

    private fun isHorizontalWorldLineBeyondPadding(worldLineValue: Int, verticalPaddingHeight: Float): Boolean {
        val yInBatchCoordinates = y + (worldLineValue - gameWorldBottomY) * batchUnitsPerGameUnit
        layout.setText(bitmapFontCache.font, worldLineValue.toString())
        val textLabelBatchBottom = yInBatchCoordinates - layout.height / 2
        val textLabelBatchTop = yInBatchCoordinates + layout.height / 2
        return textLabelBatchBottom < y + verticalPaddingHeight || textLabelBatchTop > y + height - verticalPaddingHeight
    }

    private fun isVerticalWorldLineBeyondPadding(worldLineValue: Int, horizontalPaddingWidth: Float): Boolean {
        val xInBatchCoordinates = x + (worldLineValue - gameWorldBottomX) * batchUnitsPerGameUnit
        layout.setText(bitmapFontCache.font, worldLineValue.toString())
        val textLabelBatchLeft = xInBatchCoordinates - layout.width / 2
        val textLabelBatchRight = xInBatchCoordinates + layout.width / 2
        return textLabelBatchLeft < x + horizontalPaddingWidth || textLabelBatchRight > x + width - horizontalPaddingWidth
    }

    private fun publishEvent() {
        val event = eventsPool.obtain()
        event.batchUnitsPerGameUnit = batchUnitsPerGameUnit
        event.gameWorldBottomX = gameWorldBottomX
        event.gameWorldBottomY = gameWorldBottomY
        event.gameWorldWidth = width / batchUnitsPerGameUnit
        event.gameWorldHeight = height / batchUnitsPerGameUnit
        eventPublisher.publishEvent(event)
        eventsPool.free(event)
    }
}