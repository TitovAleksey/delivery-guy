package com.funsoftware.game.deliveryguy.leveleditor.component

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LazyComponent()
