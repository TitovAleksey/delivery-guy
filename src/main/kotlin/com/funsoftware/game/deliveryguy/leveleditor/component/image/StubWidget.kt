package com.funsoftware.game.deliveryguy.leveleditor.component.image

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Widget

class StubWidget(private val widthSupplier: () -> Float, private val heightSupplier: () -> Float) : Widget() {

    private val texture: Texture;

    init {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.BLUE)
        pixmap.fillRectangle(0, 0, 1, 1)
        texture = Texture(pixmap)
        pixmap.dispose()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(texture, x, y, width, height)
    }

    override fun getPrefWidth(): Float {
        return widthSupplier()
    }

    override fun getPrefHeight(): Float {
        return heightSupplier()
    }

    override fun getMaxWidth(): Float {
        return prefWidth
    }

    override fun getMaxHeight(): Float {
        return prefHeight
    }

    override fun getMinWidth(): Float {
        return 0f
    }

    override fun getMinHeight(): Float {
        return 0f
    }
}