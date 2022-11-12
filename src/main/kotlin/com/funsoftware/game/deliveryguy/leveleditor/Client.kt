package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport

class Client : ApplicationAdapter() {

    private lateinit var uiStage: Stage
    private lateinit var gameWorldStage: Stage
    private lateinit var gameWorldStack: Stack
    private lateinit var gameWorldGrid: GameWorldGrid

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

        val viewport = ScreenViewport()
        gameWorldStage = Stage(viewport)
        gameWorldStack = Stack()
        gameWorldStack.setFillParent(true)
        gameWorldStage.addActor(gameWorldStack)
        gameWorldGrid = GameWorldGrid(skin)
        gameWorldStack.add(gameWorldGrid)

        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(uiStage)
        inputMultiplexer.addProcessor(gameWorldStage)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)
        val deltaTime = Gdx.graphics.deltaTime
        gameWorldStage.act(deltaTime)
        gameWorldStage.draw()
        //uiStage.act(deltaTime)
        //uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
        gameWorldStage.viewport.update(width, height, true)
    }
}