package com.funsoftware.game.deliveryguy.leveleditor

import java.io.File
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.name


//@Service
class ImagesService(private val projectHome: String) {

    private val images = ConcurrentHashMap<String, Path>()

    fun getAvailableImageNames(): List<String> {
        return images.keys().toList().sorted()
    }

    fun getFileAbsolutePath(filename: String): Path? {
        return images[filename]
    }

    //@PostConstruct
    fun addAlreadyExistedImagesAndStartWatcher() {
        val directory = File("$projectHome/images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val path = directory.toPath()
        addImagesToMap(path)
        startFilesWatcher(path)
    }

    private fun addImagesToMap(path: Path) {
        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (file.name.endsWith(".png"))
                    images[file.name] = path.resolve(file)
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun startFilesWatcher(path: Path) {
        val watcher = FileSystems.getDefault().newWatchService()
        path.register(watcher, arrayOf(ENTRY_CREATE, ENTRY_DELETE))
        Thread {
            while (!Thread.currentThread().isInterrupted) {
                val key = watcher.take()
                key.pollEvents().stream()
                    .filter { event -> event.kind() != OVERFLOW }
                    .forEach { event ->
                        val relativePath = (event as WatchEvent<Path>).context()
                        if (relativePath.name.endsWith(".png")) {
                            val fullPath = path.resolve(relativePath)
                            if (event.kind() == ENTRY_CREATE) {
                                images[relativePath.name] = fullPath
                            } else {
                                images.remove(relativePath.name)
                            }
                        }
                    }
                key.reset()
            }
        }.start()
    }
}