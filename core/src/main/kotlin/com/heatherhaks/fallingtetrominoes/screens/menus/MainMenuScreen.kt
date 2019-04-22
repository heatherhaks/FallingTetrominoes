package com.heatherhaks.fallingtetrominoes.screens.menus

import com.heatherhaks.fallingtetrominoes.screens.GameScreen
import ktx.inject.Context

class MainMenuScreen(context: Context) : Menu(context) {

    override fun getMenuText() : Array<String> {
        val output = arrayOf(
                "Play Game",
                "Options",
                "Exit")

        return output
    }

    override fun menuActions() {
        when(cursorY) {
            0 -> game.setScreen<GameScreen>()
            1 -> game.setScreen<OptionsMenuScreen>()
            2 -> System.exit(0)
        }
    }
}