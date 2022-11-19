package com.funsoftware.game.deliveryguy.leveleditor.component

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window

class RespectfulBoundsWindow(title: String, skin: Skin) : Window(title, skin) {

    private var processDragStop = false
    private var xBeforeDrag = 0f
    private var yBeforeDrag = 0f

    override fun validate() {
        super.validate()
        if (isDragging) {
            processDragStop = true
        } else {
            if (processDragStop) {
                invalidate()
            }
        }
    }

    override fun layout() {
        super.layout()
        if (processDragStop) {
            checkIfWindowOverlapsOtherActors()
        }
        if (!isDragging) {
            xBeforeDrag = x
            yBeforeDrag = y
        }
    }

    private fun checkIfWindowOverlapsOtherActors() {
        val isBoundsCheckRequired = parent is RespectfulBoundsGroup
        var boundsValid = true
        if (isBoundsCheckRequired) {
            boundsValid = !(parent as RespectfulBoundsGroup).isBoundaryIntersectedByOtherActor(
                this,
                x,
                y,
                width,
                height
            )
        }
        if (!boundsValid) {
            val action = MoveToAction()
            action.setPosition(xBeforeDrag, yBeforeDrag)
            addAction(action)
        }
    }
}