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
}