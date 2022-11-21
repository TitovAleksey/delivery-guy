package com.funsoftware.game.deliveryguy.leveleditor.component.image

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Widget

class ResourceImageIcon(private val texture: Texture) : Widget() {

    private val prefWidth: Float
    private val prefHeight: Float
    private val maxWidth: Float
    private val maxHeight: Float

    init {
        val batchUnitsPerCm = Gdx.graphics.density * 160f / 2.54f
        val prefSizeInCm = 2
        prefWidth = batchUnitsPerCm * prefSizeInCm
        prefHeight = prefWidth
        val maxSizeInCm = 4
        maxWidth = batchUnitsPerCm * maxSizeInCm
        maxHeight = maxWidth
    }

    override fun getPrefWidth(): Float {
        return prefWidth
    }

    override fun getPrefHeight(): Float {
        return prefHeight
    }

    override fun getMaxWidth(): Float {
        return maxWidth
    }

    override fun getMaxHeight(): Float {
        return maxHeight
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }
}