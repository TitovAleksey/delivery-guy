package com.funsoftware.game.deliveryguy.leveleditor.component

import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.properties.ReadOnlyProperty

class Widget(
    private val actor: Actor,
    private val x: Float? = null,
    private val y: Float? = null,
    private val width: Float? = null,
    private val height: Float? = null
) {

    val xVertexes: Pair<WidgetVertex, WidgetVertex> by xPairDelegate()
    val yVertexes: Pair<WidgetVertex, WidgetVertex> by yPairDelegate()

    private fun xPairDelegate(): ReadOnlyProperty<Widget, Pair<WidgetVertex, WidgetVertex>> =
        ReadOnlyProperty { thisRef, _ ->
            val fillByActorValues = thisRef.x == null || thisRef.width == null
            if (fillByActorValues)
                Pair(
                    WidgetVertex(thisRef, true, actor.x),
                    WidgetVertex(thisRef, false, actor.x + actor.width)
                )
            else
                Pair(
                    WidgetVertex(thisRef, true, thisRef.x!!),
                    WidgetVertex(thisRef, false, thisRef.x + thisRef.width!!)
                )
        }

    private fun yPairDelegate(): ReadOnlyProperty<Widget, Pair<WidgetVertex, WidgetVertex>> =
        ReadOnlyProperty { thisRef, _ ->
            val fillByActorValues = thisRef.y == null || thisRef.height == null
            if (fillByActorValues)
                Pair(
                    WidgetVertex(thisRef, true, actor.y),
                    WidgetVertex(thisRef, false, actor.y + actor.height)
                )
            else
                Pair(
                    WidgetVertex(thisRef, true, thisRef.y!!),
                    WidgetVertex(thisRef, false, thisRef.y + thisRef.height!!)
                )
        }
}