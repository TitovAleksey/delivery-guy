package com.funsoftware.game.deliveryguy.leveleditor.component.resources

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.funsoftware.game.deliveryguy.leveleditor.component.LazyComponent
import com.funsoftware.game.deliveryguy.leveleditor.component.image.ResourceImageIcon
import com.funsoftware.game.deliveryguy.leveleditor.service.ImagesService
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@LazyComponent
class ResourcesWindow(
    private val imagesService: ImagesService,
    private val dragAndDrop: DragAndDrop,
    skin: Skin
) : Window("Resources", skin) {

    private var images = mapOf<String, Path>()
    private var sortedImages = listOf<ResourceImageIcon>()
    private val contentTable = ResizableItemsTable()
    private val scrollPane: ScrollPane

    init {
        isResizable = true
        scrollPane = ScrollPane(contentTable, skin)
        scrollPane.fadeScrollBars = false
        add(scrollPane).grow().left().top()
        //todo: reference leak
        dragAndDrop.addTarget(object : DragAndDrop.Target(this) {
            override fun drag(
                source: DragAndDrop.Source?,
                payload: DragAndDrop.Payload?,
                x: Float,
                y: Float,
                pointer: Int
            ): Boolean {
                return false
            }

            override fun drop(
                source: DragAndDrop.Source?,
                payload: DragAndDrop.Payload?,
                x: Float,
                y: Float,
                pointer: Int
            ) {
                //do nothing
            }
        })
    }

    override fun validate() {
        super.validate()
        val actualImages = imagesService.getImages()
        if (actualImages != images) {
            images = actualImages
            //todo: don't create new instance if icon already exists
            sortedImages = images.values.map {
                val imageIcon = ResourceImageIcon(loadTextureFromPath(it))
                dragAndDrop.addSource(imageIcon.dragAndDropSource)
                dragAndDrop.addTarget(imageIcon.dragAndDropTarget)
                imageIcon
            }
            contentTable.clear()
            sortedImages.forEach(contentTable::addActor)
        }
    }

    private fun loadTextureFromPath(path: Path): Texture {
        return Texture(Gdx.files.absolute(path.absolutePathString()))
    }
}