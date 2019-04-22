package com.heatherhaks.fallingtetrominoes.screens.menus

import com.badlogic.gdx.Gdx
import com.heatherhaks.fallingtetrominoes.format
import com.heatherhaks.fallingtetrominoes.injection.wrappers.Sound
import com.heatherhaks.fallingtetrominoes.timers.Timer
import ktx.inject.Context

class SoundMenuScreen(context: Context) : Menu(context) {
    val soundValues = context.inject<Sound>()
    var isRepeating = false
    val repeatingTimer = Timer(0.15f)

    override fun getMenuText(): Array<String> {
        val output = mutableListOf<String>()

        output.add("Volume ${(soundValues.musicVolume * 100f).format(0)}%")
        output.add("Back")

        return output.toTypedArray()
    }

    override fun menuActions() {
        when(cursorY) {
            1 -> game.setScreen<OptionsMenuScreen>()
        }
    }

    override fun processInput() {
        if(inputHandler.isAnyKeyPressed) {
            if(menuKeys.upKey.isJustPressed()) cursorY--
            if(menuKeys.downKey.isJustPressed()) cursorY++
            if(cursorY < 0) cursorY = 0
            if(cursorY >= menuCount) cursorY = menuCount - 1
            if(menuKeys.select.isJustPressed()) menuActions()
        }
        if(cursorY == 0) {
            if(menuKeys.leftKey.isHeld()) {
                if(!isRepeating) {
                    soundValues.musicVolume -= 0.01f
                    isRepeating = true
                    if(repeatingTimer.isStopped()) repeatingTimer.start()
                } else if(repeatingTimer.isFinished()) {
                    soundValues.musicVolume -= 0.01f
                    repeatingTimer.restart()
                }
            } else if(menuKeys.leftKey.isJustReleased()) {
                isRepeating = false
                repeatingTimer.stop()
            }

            if(menuKeys.rightKey.isHeld()) {
                if(!isRepeating) {
                    soundValues.musicVolume += 0.01f
                    isRepeating = true
                    if(repeatingTimer.isStopped()) repeatingTimer.start()
                } else if(repeatingTimer.isFinished()) {
                    soundValues.musicVolume += 0.01f
                    repeatingTimer.restart()
                }
            } else if(menuKeys.rightKey.isJustReleased()) {
                isRepeating = false
                repeatingTimer.stop()
            }
        }

        repeatingTimer.update(Gdx.graphics.deltaTime)
    }
}