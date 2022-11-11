package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup

class UICanvas : WidgetGroup(), RespectfulBoundsGroup {

    private val activeWidgets = ArrayList<Widget>()

    init {
        setFillParent(true)
    }

    override fun isBoundaryIntersectedByOtherActor(
        actor: Actor,
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ): Boolean {
        val widgets = mutableListOf(Widget(actor, x, y, width, height))
        children.filter { it != actor }
            .map { Widget(it) }
            .forEach { widgets.add(it) }
        val sortedXVertexes = widgets.flatMap { it.xVertexes.toList() }
            .sortedWith(this::compareVertexes)
        return isThereAnyIntersection(sortedXVertexes)
    }

    private fun compareVertexes(v1: WidgetVertex, v2: WidgetVertex): Int {
        val result = compareValues(v1.coordinate, v2.coordinate)
        return if (result == 0) compareValues(v1.isFirst, v2.isFirst) else result
    }

    private fun isThereAnyIntersection(vertexes: List<WidgetVertex>): Boolean {
        for (vertex in vertexes) {
            if (vertex.isFirst) {
                activeWidgets.forEach { widget ->
                    if (isThereIntersectionByYScale(vertex.widget, widget)) {
                        activeWidgets.clear()
                        return true
                    }
                }
                activeWidgets.add(vertex.widget)
            } else {
                activeWidgets.remove(vertex.widget)
            }
        }
        return false
    }

    private fun isThereIntersectionByYScale(widget1: Widget, widget2: Widget): Boolean {
        return widget1.yVertexes.first.coordinate in widget2.yVertexes.first.coordinate..widget2.yVertexes.second.coordinate
                || widget1.yVertexes.second.coordinate in widget2.yVertexes.first.coordinate..widget2.yVertexes.second.coordinate
    }
}