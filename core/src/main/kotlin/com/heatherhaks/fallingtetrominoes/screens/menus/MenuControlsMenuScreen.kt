package com.heatherhaks.fallingtetrominoes.screens.menus

import com.badlogic.gdx.Input
import ktx.inject.Context

class MenuControlsMenuScreen(context: Context) : Menu(context) {
    var waitingOnInput = false

    override fun getMenuText(): Array<String> {
        val output = mutableListOf<String>()

        menuKeys.keyArray.forEach{
            output.add("${it.name} = ${Input.Keys.toString(it.keycode)}")
        }
        output.add("Back")

        return output.toTypedArray()
    }

    override fun menuActions() {
        if(cursorY == menuCount - 1) game.setScreen<ControlsMenuScreen>()
        else {
            waitingOnInput = true
        }
    }

    override fun processInput() {
        if(waitingOnInput) {
            terminal[0, -1].write("Press a key...")

            if(inputHandler.isAnyKeyPressed) {
                menuKeys.keyArray[cursorY].keycode = inputHandler.keycode
                waitingOnInput = false
            }
        }else if(inputHandler.isAnyKeyPressed) {
            if(menuKeys.upKey.isJustPressed()) cursorY--
            if(menuKeys.downKey.isJustPressed()) cursorY++
            if(cursorY < 0) cursorY = 0
            if(cursorY >= menuCount) cursorY = menuCount - 1
            if(menuKeys.select.isJustPressed()) menuActions()
        }
    }
}