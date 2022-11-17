package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ApplicationAdapterImpl(private val applicationContext: ApplicationContext) : ApplicationAdapter() {

    private lateinit var uiStage: Stage
    private lateinit var gameWorldStage: Stage

    override fun create() {
        uiStage = applicationContext.getBean("uiStage", Stage::class.java)
        gameWorldStage = applicationContext.getBean("gameWorldStage", Stage::class.java)
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(uiStage)
        inputMultiplexer.addProcessor(gameWorldStage)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)
        val deltaTime = Gdx.graphics.deltaTime
        gameWorldStage.act(deltaTime)
        uiStage.act(deltaTime)
        gameWorldStage.draw()
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
        gameWorldStage.viewport.update(width, height, true)
    }
}