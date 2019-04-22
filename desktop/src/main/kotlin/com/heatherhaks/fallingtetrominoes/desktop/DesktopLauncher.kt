@file:JvmName("DesktopLauncher")

package com.heatherhaks.fallingtetrominoes.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

import com.heatherhaks.fallingtetrominoes.FallingTetrominoes
import com.heatherhaks.fallingtetrominoes.appconstants.AppConstants

/** Launches the desktop (LWJGL) application. */
fun main(args: Array<String>) {
    LwjglApplication(FallingTetrominoes(), LwjglApplicationConfiguration().apply {
        title = AppConstants.TITLE
        width = AppConstants.WIDTH
        height = AppConstants.HEIGHT
        backgroundFPS = AppConstants.BACKGROUND_FPS
        foregroundFPS = AppConstants.FOREGROUND_FPS
        resizable = AppConstants.RESIZEABLE
        forceExit = true

        intArrayOf(128, 64, 32, 16).forEach{
            addIcon("libgdx$it.png", Files.FileType.Internal)
        }
    })
}