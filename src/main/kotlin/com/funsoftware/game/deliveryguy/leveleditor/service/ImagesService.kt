package com.funsoftware.game.deliveryguy.leveleditor.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct
import kotlin.io.path.name

@Service
class ImagesService(@Value("\${project.home:/home/aleksey}") private val projectHome: String) {

    private val images = ConcurrentHashMap<String, Path>()
    private val path: Path

    init {
        val directory = File("$projectHome/images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        path = directory.toPath()
    }

    @PostConstruct
    fun addAlreadyExistedImagesAndStartWatcher() {
        addImagesToMap()
        startFilesWatcher()
    }

    private fun addImagesToMap() {
        Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (file.name.endsWith(".png"))
                    images[file.name] = file
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun startFilesWatcher() {
        val watcher = FileSystems.getDefault().newWatchService()
        path.register(watcher, arrayOf(ENTRY_CREATE, ENTRY_DELETE))
        val thread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                val key = watcher.take()
                key.pollEvents().stream()
                    .filter { event -> event.kind() != OVERFLOW }
                    .forEach { event ->
                        val pathEvent = (event as WatchEvent<Path>).context()
                        if (pathEvent.name.endsWith(".png")) {
                            when (event.kind()) {
                                ENTRY_CREATE -> images[pathEvent.name] = path.resolve(pathEvent)
                                ENTRY_DELETE -> images.remove(pathEvent.name)
                            }
                        }
                    }
                key.reset()
            }
        }
        thread.isDaemon = true
        thread.start()
    }

    fun getImages(): Map<String, Path> {
        return images.toMap()
    }
}