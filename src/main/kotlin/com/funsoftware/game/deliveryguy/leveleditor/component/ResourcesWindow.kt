package com.funsoftware.game.deliveryguy.leveleditor.component

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.funsoftware.game.deliveryguy.leveleditor.service.ImagesService
import java.nio.file.Path

@LazyComponent
class ResourcesWindow(private val imagesService: ImagesService, skin: Skin) :
    RespectfulBoundsWindow("Resources", skin) {

    private var images: Map<String, Path> = mapOf()

    init {
        isResizable = true
    }

    override fun validate() {
        super.validate()
        val actualImages = imagesService.getImages()
        if (actualImages != images) {
            images = actualImages
            images.keys.forEach(this::add)
            invalidate()
        }
    }
}