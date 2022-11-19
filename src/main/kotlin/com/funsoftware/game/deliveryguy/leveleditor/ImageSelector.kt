package com.funsoftware.game.deliveryguy.leveleditor

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import java.nio.file.Path
import java.util.function.Consumer
import kotlin.streams.toList

class ImageSelector(
    skin: Skin,
    private val imagesService: ImagesService,
    private val selectedImageHandler: Consumer<Path>
) : Dialog("Select an image", skin) {

    private val SELECT_IMAGE_RESULT = "SELECT_IMAGE_RESULT"

    private val imageContainer = Container<Image>()
    private val imageList = List<String>(skin)
    private val okButton = TextButton("OK", skin)
    private var selectedFile: Path? = null

    init {
        addContent()
        addButtons()
    }

    private fun addContent() {
        addImageSelector()
        addImagePreview()
    }

    private fun addImageSelector() {
        val table = Table()
        contentTable.add(table).align(Align.topLeft)
        addSearchPanel(table)
        addImageList(table)
    }

    private fun addSearchPanel(table: Table) {
        val horizontalGroup = HorizontalGroup()
        table.add(horizontalGroup).align(Align.topRight)
        table.row()
        val label = Label("Search:", skin)
        horizontalGroup.addActor(label)
        val textField = TextField("", skin)
        textField.setTextFieldListener { textFieldEvent, _ -> refreshImageList(textFieldEvent.text) }
        horizontalGroup.addActor(textField)
    }

    private fun addImageList(table: Table) {
        table.add(imageList).align(Align.topRight)
        table.row()
        imageList.addListener { event ->
            if (event is ChangeListener.ChangeEvent && event.target == imageList) {
                showSelectedImage()
                true
            } else {
                false
            }
        }
        refreshImageList()
    }

    private fun refreshImageList(filenameFilter: String = "") {
        val sortedFileNameList = imagesService.getAvailableImageNames().stream()
            .filter { filename -> if (filenameFilter.isEmpty()) true else filename.contains(filenameFilter) }
            .toList()
        imageList.setItems(*sortedFileNameList.toTypedArray())
        showSelectedImage()
    }

    private fun showSelectedImage() {
        if (imageList.selected != null) {
            val path = imagesService.getFileAbsolutePath(imageList.selected)
            imageContainer.actor = Image(Texture(FileHandle(path.toString())))
            okButton.isDisabled = false
            selectedFile = path
        } else {
            imageContainer.clear()
            okButton.isDisabled = true
            selectedFile = null
        }
    }

    private fun addImagePreview() {
        contentTable.add(imageContainer).width(200f).height(200f).align(Align.topLeft)
    }

    private fun addButtons() {
        buttonTable.align(Align.topRight)
        button(okButton, SELECT_IMAGE_RESULT)
        button("Cancel")
    }

    override fun result(`object`: Any?) {
        if (`object` is String && `object` == SELECT_IMAGE_RESULT)
            selectedImageHandler.accept(selectedFile!!)
    }
}