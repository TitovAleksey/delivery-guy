package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

class RespectfulBoundsWindow(title: String, skin: Skin) : Window(title, skin) {

    override fun setBounds(x: Float, y: Float, width: Float, height: Float) {
        var isBoundsCheckRequired = parent is RespectfulBoundsGroup
        var boundsValid = true
        if (this.x != x || this.y != y) {
            if (isBoundsCheckRequired) {
                boundsValid = !(parent as RespectfulBoundsGroup).isBoundaryIntersectedByOtherActor(
                    this, x, y, width, height
                )
                isBoundsCheckRequired = false
            }
            if (boundsValid) {
                this.x = x
                this.y = y
                positionChanged()
            }
        }
        if (this.width != width || this.height != height) {
            if (isBoundsCheckRequired) {
                boundsValid = !(parent as RespectfulBoundsGroup).isBoundaryIntersectedByOtherActor(
                    this, x, y, width, height
                )
            }
            if (boundsValid) {
                this.width = width
                this.height = height
                sizeChanged()
            }
        }
    }
}