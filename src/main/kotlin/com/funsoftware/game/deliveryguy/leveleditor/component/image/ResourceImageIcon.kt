package com.funsoftware.game.deliveryguy.leveleditor.component.image

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Widget

class ResourceImageIcon(private val texture: Texture) : Widget() {

    private val prefSize: Float

    init {
        val batchUnitsPerCm = Gdx.graphics.density * 160f / 2.54f
        val prefSizeInCm = 2
        prefSize = batchUnitsPerCm * prefSizeInCm
    }

    override fun getPrefWidth(): Float {
        return prefSize
    }

    override fun getPrefHeight(): Float {
        return prefSize
    }

    override fun getMaxWidth(): Float {
        return 0f
    }

    override fun getMaxHeight(): Float {
        return 0f
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }
}