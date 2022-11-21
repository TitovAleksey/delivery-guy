package com.funsoftware.game.deliveryguy.leveleditor.component.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.funsoftware.game.deliveryguy.leveleditor.component.LazyComponent
import com.funsoftware.game.deliveryguy.leveleditor.component.RespectfulBoundsWindow
import com.funsoftware.game.deliveryguy.leveleditor.component.image.ResourceImageIcon
import com.funsoftware.game.deliveryguy.leveleditor.service.ImagesService
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@LazyComponent
class ResourcesWindow(private val imagesService: ImagesService, skin: Skin) :
    RespectfulBoundsWindow("Resources", skin) {

    private var images: Map<String, Path> = mapOf()
    private val contentTable = Table()

    init {
        isResizable = true
        contentTable.debug = true
        val scrollPane = ScrollPane(contentTable, skin)
        scrollPane.fadeScrollBars = false
        add(scrollPane).fill().expand()
    }

    override fun validate() {
        super.validate()
        val actualImages = imagesService.getImages()
        if (actualImages != images) {
            images = actualImages
            images.keys.forEach {
                val imageIcon = ResourceImageIcon(loadTextureFromPath(images[it]!!))
                contentTable.add(imageIcon).fill().expand()
            }
            invalidate()
        }
    }

    private fun loadTextureFromPath(path: Path): Texture {
        return Texture(Gdx.files.absolute(path.absolutePathString()))
    }
}