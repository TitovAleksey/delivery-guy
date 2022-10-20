package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport

class Client : ApplicationAdapter() {

    private lateinit var stage: Stage

    override fun create() {
        stage = Stage(ScreenViewport())
        Gdx.input.inputProcessor = stage
        val uiCanvas = UICanvas()
        stage.addActor(uiCanvas)
        val skin = Skin(Gdx.files.internal("uiskin.json"))
        val window = AutoMovableWindow("first window", skin)
        window.isResizable = true
        uiCanvas.addWidget(window)
        uiCanvas.addWidget(AutoMovableWindow("second window", skin))
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)
        val deltaTime = Gdx.graphics.deltaTime
        stage.act(deltaTime)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }
}