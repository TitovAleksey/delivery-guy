package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.funsoftware.game.deliveryguy.leveleditor.Client

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setMaximized(true)
    Lwjgl3Application(Client(), config)
}