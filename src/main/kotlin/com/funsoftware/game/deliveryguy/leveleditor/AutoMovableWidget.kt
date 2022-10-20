package com.funsoftware.game.deliveryguy.leveleditor

interface AutoMovableWidget {

    fun getXVertexes(): Pair<WidgetVertex, WidgetVertex>
    fun getYVertexes(): Pair<WidgetVertex, WidgetVertex>
    var isAutoMovable: Boolean
}