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

    private var images = mapOf<String, Path>()
    private var sortedImages = listOf<ResourceImageIcon>()
    private var imagesPerRow = 1
    private val contentTable = Table()

    init {
        isResizable = true
        contentTable.debug = true
        val scrollPane = ScrollPane(contentTable, skin)
        scrollPane.fadeScrollBars = false
        add(scrollPane).grow().left().top()
    }

    override fun validate() {
        super.validate()
        var isValid = true
        val actualImages = imagesService.getImages()
        if (actualImages != images) {
            images = actualImages
            sortedImages = images.values.map { ResourceImageIcon(loadTextureFromPath(it)) }
            isValid = false
        }
        if (sortedImages.isNotEmpty()) {
            if (sortedImages[0].minWidth * (imagesPerRow + 1) <= width) {
                imagesPerRow++
                isValid = false
            } else if (imagesPerRow > 1 && sortedImages[0].minWidth * imagesPerRow > width) {
                imagesPerRow--
                isValid = false
            }
        }
        if (!isValid) {
            contentTable.clear()
            var counter = 1
            sortedImages.forEach {
                contentTable.add(it).grow().top().left()
                if (++counter > imagesPerRow) {
                    contentTable.row()
                    counter = 1
                }
            }
        }
    }

    private fun loadTextureFromPath(path: Path): Texture {
        return Texture(Gdx.files.absolute(path.absolutePathString()))
    }
}