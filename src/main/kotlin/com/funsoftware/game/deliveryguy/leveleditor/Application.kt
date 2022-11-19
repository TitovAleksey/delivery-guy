package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.funsoftware.game.deliveryguy.leveleditor.component.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy

@SpringBootApplication
class Application {

    @Bean
    fun runApp(applicationAdapter: ApplicationAdapterImpl): Any {
        val uiThread = Thread {
            val config = Lwjgl3ApplicationConfiguration()
            config.setMaximized(true)
            Lwjgl3Application(applicationAdapter, config)
        }
        uiThread.start()
        return Any()
    }

    @Bean
    @Lazy
    fun skin(): Skin {
        return Skin(Gdx.files.internal("uiskin.json"))
    }

    @Bean
    @Lazy
    fun uiStage(uiCanvas: UICanvas, gameWorldGrid: GameWorldGrid, skin: Skin): Stage {
        val stage = Stage(ScreenViewport())
        val stack = Stack()

        stack.setFillParent(true)
        stack.add(gameWorldGrid)
        stack.add(uiCanvas)
        stage.root = stack

        uiCanvas.addActor(RespectfulBoundsWindow("window 1", skin))
        uiCanvas.addActor(RespectfulBoundsWindow("window 2", skin))

        return stage
    }

    @Bean
    @Lazy
    fun gameWorldStage(gameWorldCanvas: GameWorldCanvas, testWidget: TestWidget): Stage {
        val stage = Stage(ScreenViewport())
        stage.root = gameWorldCanvas
        gameWorldCanvas.addActor(testWidget)
        return stage
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
        .headless(false)
        .run(*args)
}