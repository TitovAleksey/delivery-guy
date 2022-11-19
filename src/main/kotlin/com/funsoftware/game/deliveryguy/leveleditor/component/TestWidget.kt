package com.funsoftware.game.deliveryguy.leveleditor.component

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Widget

@LazyComponent
class TestWidget : Widget() {

    private val sprite: Sprite

    init {
        val pixmap = Pixmap(2, 2, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.RED)
        pixmap.fillRectangle(0, 0, 2, 2)
        val texture = Texture(pixmap)
        pixmap.dispose()
        sprite = Sprite(texture)
        sprite.setSize(0.01f, 0.03f)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        sprite.draw(batch)
    }
}