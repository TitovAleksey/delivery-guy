package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.funsoftware.game.deliveryguy.leveleditor.component.GameWorldCanvas
import com.funsoftware.game.deliveryguy.leveleditor.component.GameWorldGrid
import com.funsoftware.game.deliveryguy.leveleditor.component.TestWidget
import com.funsoftware.game.deliveryguy.leveleditor.component.UICanvas
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
    fun uiStage(uiCanvas: UICanvas): Stage {
        val stage = Stage(ScreenViewport())
        stage.root = uiCanvas
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

    @Bean
    @Lazy
    fun dragAndDrop(): DragAndDrop {
        return DragAndDrop()
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
        .headless(false)
        .run(*args)
}