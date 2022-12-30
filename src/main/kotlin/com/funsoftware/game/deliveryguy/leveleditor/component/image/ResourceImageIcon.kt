package com.funsoftware.game.deliveryguy.leveleditor.component.image

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target

class ResourceImageIcon(private val texture: Texture) : Widget() {

    val dragAndDropSource: Source = createDragAndDropSource()
    val dragAndDropTarget: Target = createDragAndDropTarget()
    private val prefSize = calculatePrefSize()

    //todo: find something more suitable
    private val validImage = Image(texture)
    private val invalidImage = Image(texture)

    private fun calculatePrefSize(): Float {
        val batchUnitsPerCm = Gdx.graphics.density * 160f / 2.54f
        val prefSizeInCm = 2.5f
        return batchUnitsPerCm * prefSizeInCm
    }

    private fun createDragAndDropSource(): Source {
        return object : Source(this) {
            override fun dragStart(event: InputEvent?, x: Float, y: Float, pointer: Int): Payload {
                val payload = Payload()
                payload.dragActor = actor
                payload.`object` = texture
                payload.validDragActor = validImage
                payload.invalidDragActor = invalidImage
                validImage.setSize(width, height)
                invalidImage.setSize(width, height)
                return payload
            }
        }
    }

    private fun createDragAndDropTarget(): Target {
        return object : Target(this) {
            override fun drag(source: Source?, payload: Payload?, x: Float, y: Float, pointer: Int): Boolean {
                return false
            }

            override fun drop(source: Source?, payload: Payload?, x: Float, y: Float, pointer: Int) {
                //do nothing
            }
        }
    }

    override fun getPrefWidth(): Float {
        return prefSize
    }

    override fun getPrefHeight(): Float {
        return prefSize
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }
}