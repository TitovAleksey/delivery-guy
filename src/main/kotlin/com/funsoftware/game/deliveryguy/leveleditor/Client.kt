package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport

class Client : ApplicationAdapter() {

    private lateinit var uiStage: Stage
    private lateinit var gameWorldGridStage: Stage
    private lateinit var gameWorldCanvasStage: Stage

    override fun create() {
        uiStage = Stage(ScreenViewport())
        val uiCanvas = UICanvas()
        uiStage.addActor(uiCanvas)
        val skin = Skin(Gdx.files.internal("uiskin.json"))
        val window = RespectfulBoundsWindow("first window", skin)
        window.isResizable = true
        window.setPosition(300f, 300f)
        uiCanvas.addActor(window)
        uiCanvas.addActor(RespectfulBoundsWindow("second window", skin))

        var viewport = ScreenViewport()
        gameWorldGridStage = Stage(viewport)
        val gameWorldGrid = GameWorldGrid(skin)
        gameWorldGridStage.scrollFocus = gameWorldGrid
        gameWorldGridStage.keyboardFocus = gameWorldGrid
        gameWorldGridStage.addActor(gameWorldGrid)

        viewport = ScreenViewport()
        gameWorldCanvasStage = Stage(viewport)
        val gameWorldCanvas = GameWorldCanvas()
        gameWorldCanvasStage.addActor(gameWorldCanvas)
        gameWorldCanvas.addActor(TestWidget())

        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(uiStage)
        inputMultiplexer.addProcessor(gameWorldGridStage)
        inputMultiplexer.addProcessor(gameWorldCanvasStage)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)
        val deltaTime = Gdx.graphics.deltaTime
        gameWorldGridStage.act(deltaTime)
        gameWorldCanvasStage.act(deltaTime)
        gameWorldGridStage.draw()
        gameWorldCanvasStage.draw()
        //uiStage.act(deltaTime)
        //uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
        gameWorldGridStage.viewport.update(width, height, true)
        gameWorldCanvasStage.viewport.update(width, height, true)
    }
}