package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.scenes.scene2d.Actor

interface RespectfulBoundsGroup {

    fun isBoundaryIntersectedByOtherActor(actor: Actor, x: Float, y: Float, width: Float, height: Float): Boolean
}