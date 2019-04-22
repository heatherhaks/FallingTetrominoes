package com.heatherhaks.fallingtetrominoes.screens.menus

import ktx.inject.Context

class ControlsMenuScreen(context: Context) : Menu(context) {
    override fun getMenuText(): Array<String> {
        val output = arrayOf(
                "Game Controls",
                "Menu Controls",
                "Back")

        return output
    }

    override fun menuActions() {
        when(cursorY) {
            0 -> game.setScreen<GameControlsMenuScreen>()
            1 -> game.setScreen<MenuControlsMenuScreen>()
            2 -> game.setScreen<OptionsMenuScreen>()
        }
    }
}