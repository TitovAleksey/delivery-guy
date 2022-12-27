package com.funsoftware.game.deliveryguy.leveleditor.component.resources

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Layout
import java.util.*

class ResizableItemsTable : WidgetGroup() {

    private var preferableWidth = 0f
    private var preferableHeight = 0f

    override fun getPrefWidth(): Float {
        return preferableWidth
    }

    override fun getPrefHeight(): Float {
        return preferableHeight
    }

    override fun layout() {
        if (children.isEmpty) {
            preferableWidth = 0f
            preferableHeight = 0f
            invalidateHierarchy()
        } else {
            val actors = children.begin()
            var cellSize = calculateCellSize(actors)
            val actorsInRow = (width / cellSize).toInt().coerceAtLeast(1).coerceAtMost(children.size)
            val remainder = width - (cellSize * actorsInRow)
            if (remainder > 0) {
                cellSize += (remainder / actorsInRow)
            }
            var idxInRow = 0
            var verticalOffset = cellSize
            for (idx in 0 until children.size) {
                val actor = actors[idx]
                if (actor is Layout) {
                    val actorWidth = if (actor.maxWidth != 0f) cellSize.coerceAtMost(actor.maxWidth) else cellSize
                    val actorHeight =
                        if (actor.maxHeight != 0f) actorWidth.coerceAtMost(actor.maxHeight) else actorWidth
                    actor.setBounds(idxInRow * cellSize, height - verticalOffset, actorWidth, actorHeight)
                    if (idxInRow == actorsInRow - 1) {
                        idxInRow = 0
                        if (idx != children.size - 1) verticalOffset += cellSize
                    } else {
                        idxInRow++
                    }
                }
            }
            children.end()
            preferableWidth = cellSize
            preferableHeight = verticalOffset
            invalidateHierarchy()
        }
    }

    private fun calculateCellSize(actors: Array<Actor>): Float {
        return Arrays.stream(actors)
            .filter { it is Layout }
            .map { it as Layout }
            .map(Layout::getMinWidth)
            .min(Float::compareTo)
            .orElse(0f)
    }
}