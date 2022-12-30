package com.funsoftware.game.deliveryguy.leveleditor.component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.funsoftware.game.deliveryguy.leveleditor.component.resources.ResourcesWindow
import javax.annotation.PostConstruct

@LazyComponent
class UICanvas(
    resourcesWindow: ResourcesWindow,
    private val dragAndDrop: DragAndDrop,
    private val gameWorldGrid: GameWorldGrid
) : WidgetGroup() {

    init {
        setFillParent(true)
        addActor(gameWorldGrid)
        addActor(resourcesWindow)
        touchable = Touchable.childrenOnly
    }

    @PostConstruct
    fun fillDragAndDropTarget() {
        val dragAndDropTarget = object : DragAndDrop.Target(gameWorldGrid) {
            override fun drag(
                source: DragAndDrop.Source,
                payload: DragAndDrop.Payload,
                x: Float,
                y: Float,
                pointer: Int
            ): Boolean {
                return payload.`object` is Texture
            }

            override fun drop(
                source: DragAndDrop.Source,
                payload: DragAndDrop.Payload,
                x: Float,
                y: Float,
                pointer: Int
            ) {
                Gdx.app.log("some tag", "dropped texture")
            }
        }
        dragAndDrop.addTarget(dragAndDropTarget)
    }
}