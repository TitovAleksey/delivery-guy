package com.funsoftware.game.deliveryguy.leveleditor.component.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.funsoftware.game.deliveryguy.leveleditor.component.LazyComponent
import com.funsoftware.game.deliveryguy.leveleditor.component.RespectfulBoundsWindow
import com.funsoftware.game.deliveryguy.leveleditor.component.image.ResourceImageIcon
import com.funsoftware.game.deliveryguy.leveleditor.service.ImagesService
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@LazyComponent
class ResourcesWindow(private val imagesService: ImagesService, skin: Skin) :
    RespectfulBoundsWindow("Resources", skin) {

    private var images = mapOf<String, Path>()
    private var sortedImages = listOf<ResourceImageIcon>()
    private val contentTable = ResizableItemsTable()
    private val scrollPane: ScrollPane

    init {
        isResizable = true
        scrollPane = ScrollPane(contentTable, skin)
        scrollPane.fadeScrollBars = false
        add(scrollPane).grow().left().top()
    }

    override fun validate() {
        super.validate()
        val actualImages = imagesService.getImages()
        if (actualImages != images) {
            images = actualImages
            sortedImages = images.values.map { ResourceImageIcon(loadTextureFromPath(it)) }
            contentTable.clear()
            sortedImages.forEach(contentTable::addActor)
        }
    }

    private fun loadTextureFromPath(path: Path): Texture {
        return Texture(Gdx.files.absolute(path.absolutePathString()))
    }
}