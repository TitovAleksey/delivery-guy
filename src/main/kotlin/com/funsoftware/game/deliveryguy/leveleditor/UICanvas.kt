package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.Layout

class UICanvas : WidgetGroup() {

    init {
        setFillParent(true)
    }

    fun <T> addWidget(widget: T) where T : AutoMovableWidget, T : Actor, T : Layout {
        super.addActor(widget)
    }

    override fun layout() {
        var intersectionCount = 0
        val activeWidgets = HashSet<AutoMovableWidget>()
        children.map { it as AutoMovableWidget }
            .flatMap { it.getXVertexes().toList() }
            .sortedWith { v1, v2 ->
                val result = compareValues(v1.coordinate, v2.coordinate)
                if (result == 0) compareValues(v1.isFirst, v2.isFirst) else result
            }
            .forEach { vertex ->
                if (vertex.isFirst) {
                    activeWidgets.forEach { widget ->
                        if (isThereIntersectionByYScale(vertex.widget, widget)) {
                            intersectionCount++
                        }
                    }
                    activeWidgets.add(vertex.widget)
                } else {
                    activeWidgets.remove(vertex.widget)
                }
            }
        Gdx.app.log("some tag", "Intersections count: $intersectionCount")
    }

    private fun isThereIntersectionByYScale(widget1: AutoMovableWidget, widget2: AutoMovableWidget): Boolean {
        val widget1Vertexes = widget1.getYVertexes()
        val widget2Vertexes = widget2.getYVertexes()
        return widget1Vertexes.first.coordinate in widget2Vertexes.first.coordinate..widget2Vertexes.second.coordinate
                || widget1Vertexes.second.coordinate in widget2Vertexes.first.coordinate..widget2Vertexes.second.coordinate
                || (widget1Vertexes.second.coordinate >= widget2Vertexes.second.coordinate
                && widget1Vertexes.first.coordinate <= widget2Vertexes.first.coordinate)
    }
}