package com.funsoftware.game.deliveryguy.leveleditor

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
        val sortedXVertexes = children.map { it as AutoMovableWidget }
            .flatMap { it.getXVertexes().toList() }
            .sortedWith { v1, v2 ->
                val result = compareValues(v1.coordinate, v2.coordinate)
                if (result == 0) compareValues(v1.isFirst, v2.isFirst) else result
            }
            .toList()
    }
}