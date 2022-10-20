package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

class AutoMovableWindow(title: String, skin: Skin) : Window(title, skin), AutoMovableWidget {

    private val xFirstVertex: WidgetVertex = WidgetVertex(this, x, true)
    private val xSecondVertex: WidgetVertex = WidgetVertex(this, x + width)
    private val yFirstVertex: WidgetVertex = WidgetVertex(this, y, true)
    private val ySecondVertex: WidgetVertex = WidgetVertex(this, y + height)
    override var isAutoMovable = true

    override fun getXVertexes(): Pair<WidgetVertex, WidgetVertex> {
        return Pair(xFirstVertex, xSecondVertex)
    }

    override fun getYVertexes(): Pair<WidgetVertex, WidgetVertex> {
        return Pair(yFirstVertex, ySecondVertex)
    }

    override fun sizeChanged() {
        changeVertexCoordinatesAndInvalidateHierarchy()
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    private fun changeVertexCoordinatesAndInvalidateHierarchy() {
        xFirstVertex?.coordinate = x
        xSecondVertex?.coordinate = x + width
        yFirstVertex?.coordinate = y
        ySecondVertex?.coordinate = y + height
        invalidateHierarchy()
    }

    override fun positionChanged() {
        changeVertexCoordinatesAndInvalidateHierarchy()
    }
}